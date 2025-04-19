package com.huaifang.yan;

import com.huaifang.yan.constants.PolicySyncConstants;
import com.huaifang.yan.enums.PolicySyncStatusEnum;
import com.huaifang.yan.enums.SerializerTypeEnum;
import com.huaifang.yan.main.PolicySync;
import com.huaifang.yan.model.PolicyConfig;
import com.huaifang.yan.model.PolicyEvent;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbOption;
import com.huaifang.yan.rocksdb.RocksDbStore;
import com.huaifang.yan.sync.PolicySyncProxy;
import com.huaifang.yan.utils.LoggerUtils;
import com.huaifang.yan.utils.ProtocolConverter;
import com.huaifang.yan.utils.SerializerUtils;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.base.model.BaseModel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 政策同步处理器
 *
 * @author xianyan.geng
 * @version PolicySyncHandler, v 0.1 2020/10/20 14:42 xianyan.geng Exp $
 */
@Slf4j
public class DefaultPolicySyncer<T> implements PolicySyncer {

    /**
     * The Policy config.
     */
    private PolicyConfig policyConfig;
    /** Rocksdb库对象 */
    private RocksDbStore store;
    /** 政策同步服务 */
    private PolicySync policySync;
    /** 搜索配置包装类 */
    private PolicySearchWrapper searchWrapper;
    /** 回调事件集 */
    private final Collection<PolicyEvent> events = new ArrayList<>();

    private DefaultPolicySyncer() {
    }

    public DefaultPolicySyncer(PolicyConfig policyConfig){
        this.policyConfig = policyConfig;
    }

    @Override
    public void init() throws Exception {
        initRocksdb(policyConfig);
        this.searchWrapper = getSearchWrapper(policyConfig);
        this.policySync = new PolicySyncProxy(store, searchWrapper);
        buildColumnFamily();
        LoggerUtils.info(log, "[{}]The PolicySyncHandler is OK!", searchWrapper.getPolicySyncTypeName());
    }

    /**
     * 重建 DB
     *
     * @throws Exception Exception
     */
    @Override
    public void rebuild() throws Exception {
        initRocksdb(policyConfig);
        policySync.replaceStore(store);
        PolicySearchWrapper.ProcessorStatePair statePair = getProcessorStatePair(policyConfig);
        policySync.updateStatePair(statePair);
        buildColumnFamily();
        LoggerUtils.info(log, "[{}]rebuild db finish，data size is:{},revision is:{}", searchWrapper.getPolicySyncTypeName(), getRocksdbKeySize(), statePair.getRevision());
    }

    /**
     *  启动数据同步
     */
    @Override
    public void start() {
        policySync.start();
        LoggerUtils.info(log, "[{}]The PolicySyncHandler is started!", searchWrapper.getPolicySyncTypeName());
    }


    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    @Override
    public void executorShutdown() {
        policySync.executorShutdown();
    }

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    public boolean isExecutorTerminated() {
        return policySync.isExecutorTerminated();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        policySync.close();
    }

    /**
     * 政策同步/上报 任务是否已暂停
     *
     * @return the boolean
     */
    @Override
    public boolean isSyncStop() {
        return policySync.isSyncStop();
    }

    @Override
    public void forEach(Function function) {
        try {
            PolicySyncTypeEnum policySyncType = searchWrapper.getPolicySyncType();
            SerializerTypeEnum serializerType = searchWrapper.getPolicyConfig().getSerializerType();
            store.forEach(policySyncType.name(), (key, data) -> {
                BaseModel policy;
                try {
                    policy = BaseModel.class.cast(SerializerUtils.deSerializer(serializerType, data, policySyncType.getClazz()));
                } catch (Throwable e) {
                    policy = BaseModel.class.cast(SerializerUtils.deSerializer(SerializerTypeEnum.KRYO, data, policySyncType.getClazz()));
                }
                return (Boolean) function.apply(policy);
            });
        } catch (Exception e) {
            LoggerUtils.error(log, "[{}]遍历Rocksdb列族数据条数异常：", e, searchWrapper.getPolicySyncTypeName());
        }
    }

    @Override
    public T get(String id) {
        PolicySyncTypeEnum policySyncType = searchWrapper.getPolicySyncType();
        byte[] bytes = new byte[0];
        try {
            bytes = store.get(policySyncType.name(), id.getBytes(SerializerUtils.DEFAULT_CHARSET));
        } catch (Exception e) {
            LoggerUtils.error(log, "[{}]根据ID{}读取Rocksdb列族数据条数异常：", e, searchWrapper.getPolicySyncTypeName(), id);
        }
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return (T) SerializerUtils.deSerializer(searchWrapper.getPolicyConfig().getSerializerType(), bytes, policySyncType.getClazz());
    }

    @Override
    public long getRocksdbKeySize() {
        try {
            return store.getKeyNum(searchWrapper.getPolicySyncTypeName());
        } catch (Exception e) {
            LoggerUtils.error(log, "[{}]读取Rocksdb列族数据条数异常：", e, searchWrapper.getPolicySyncTypeName());
        }
        return NumberUtils.LONG_ZERO;
    }

    /**
     * 先暂停政策同步/上报线程池，避免db读取/写入操作，然后关闭数据库
     */
    @Override
    public void destroy() {
        threadSleep(1000);
        int count = 60;
        while (count-- > 0) {
            if (isSyncStop()) {
                store.close();
                threadSleep(1000);
                break;
            }
            threadSleep(1000);
        }
    }

    /**
     * Thread sleep.
     *
     * @param millis the millis
     */
    private void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LoggerUtils.error(log, "thread sleep happen exception", e);
        }
    }

    /**
     * 添加回调事件
     * @param consumer
     */
    @Override
    public void addCallbackEvent(Consumer consumer) {
        events.add(new PolicyEvent(searchWrapper.getPolicySyncType(), consumer));
    }

    @Override
    public PolicySyncTypeEnum getPolicySyncType() {
        return searchWrapper.getPolicySyncType();
    }

    @Override
    public PolicyConfig getPolicyConfig() {
        return policyConfig;
    }

    /**
     * 初始化rocksdb
     * @param policyConfig
     * @throws Exception
     */
    private void initRocksdb(PolicyConfig policyConfig) throws Exception {
        // 创建Rocksdb文件
        mkdirRocksdbFile(policyConfig);
        // 初始化Rocksdb数据库
        RocksDbOption option = new RocksDbOption();
        option.setPath(policyConfig.getPath());
        store = new RocksDbStore(option);
        // 创建同步进度版本列族
        if (!store.existsColumnFamily(PolicySyncConstants.COLUMN_POLICY_VERSION)) {
            store.createColumnFamily(PolicySyncConstants.COLUMN_POLICY_VERSION);
        }
    }

    /**
     * 创建Rocksdb文件
     * @param policyConfig
     * @throws Exception
     */
    private void mkdirRocksdbFile(PolicyConfig policyConfig) throws Exception {
        if (StringUtils.isBlank(policyConfig.getPath())) {
            throw new Exception("Rocksdb数据存储文件夹不能为空!");
        }
        File file = new File(policyConfig.getPath());
        if (!file.exists()) {
            FileUtils.forceMkdir(file);
        }
    }

    /**
     * 构建政策类型包装类集
     * @param policyConfig
     * @return
     */
    private PolicySearchWrapper getSearchWrapper(PolicyConfig policyConfig) throws Exception {
        byte[] typeName = policyConfig.getPolicySyncType().name().getBytes(SerializerUtils.DEFAULT_CHARSET);
        byte[] bytes = store.get(PolicySyncConstants.COLUMN_POLICY_VERSION, typeName);
        long revision = 0;
        PolicySyncStatusEnum policyStatus = PolicySyncStatusEnum.INIT;
        if (bytes != null && bytes.length > 0) {
            revision = ProtocolConverter.bytes2Long(bytes);
            policyStatus = PolicySyncStatusEnum.DELAY;
        }
        PolicySearchWrapper.ProcessorStatePair statePair = new PolicySearchWrapper.ProcessorStatePair(policyConfig.getSearchType(), revision, policyStatus, new Date(0));
        return new PolicySearchWrapper(policyConfig, policyConfig.getPolicySyncType(), statePair, events);
    }

    /**
     * Gets processor state pair.
     *
     * @param policyConfig the policy config
     * @return the processor state pair
     * @throws Exception the exception
     */
    private PolicySearchWrapper.ProcessorStatePair getProcessorStatePair(PolicyConfig policyConfig) throws Exception {
        byte[] typeName = policyConfig.getPolicySyncType().name().getBytes(SerializerUtils.DEFAULT_CHARSET);
        byte[] bytes = store.get(PolicySyncConstants.COLUMN_POLICY_VERSION, typeName);
        long revision = 0;
        PolicySyncStatusEnum policyStatus = PolicySyncStatusEnum.INIT;
        if (bytes != null && bytes.length > 0) {
            revision = ProtocolConverter.bytes2Long(bytes);
            policyStatus = PolicySyncStatusEnum.DELAY;
        }
        return new PolicySearchWrapper.ProcessorStatePair(policyConfig.getSearchType(), revision, policyStatus, new Date(0));
    }

    /**
     * 构建Rocksdb列族
     * @throws Exception
     */
    private void buildColumnFamily() throws Exception {
        // 当前列族不存在Rocksdb则直接创建
        if (!store.existsColumnFamily(searchWrapper.getPolicySyncType().getName())) {
            store.createColumnFamily(searchWrapper.getPolicySyncType().getName());
        }
        for (PolicySyncTypeEnum syncType : PolicySyncTypeEnum.values()) {
            if (syncType == searchWrapper.getPolicySyncType()) {
                continue;
            }
            // 本次启动没有配置的政策对应的列族统一删除
            if (store.existsColumnFamily(syncType.getName())) {
                store.dropColumnFamily(syncType.getName());
            }
        }
    }

    public PolicySearchWrapper getSearchWrapper() {
        return searchWrapper;
    }
}
