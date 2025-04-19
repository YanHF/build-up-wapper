package com.huaifang.yan.sync;


import com.alibaba.fastjson.JSON;
import com.huaifang.yan.main.PolicySync;
import com.huaifang.yan.main.PolicySyncClient;
import com.huaifang.yan.constants.PolicySyncConstants;
import com.huaifang.yan.enums.PolicySyncStatusEnum;
import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.huaifang.yan.model.*;
import com.huaifang.yan.rocksdb.RocksDbStore;
import com.huaifang.yan.utils.LoggerUtils;
import com.huaifang.yan.utils.ProtocolConverter;
import com.huaifang.yan.utils.SerializerUtils;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.base.model.BaseModel;
import com.ly.flight.xpgs.base.model.policy.BasePolicyDO;
import com.ly.flight.xpgs.policy.model.IndexKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 政策同步服务抽象类
 *
 * @author xianyan.geng
 * @version AbstractPolicySync, v 0.1 2020/10/20 13:46 xianyan.geng Exp $
 */
@Slf4j
public abstract class AbstractPolicySync implements PolicySync {

    private final static int              EVENT_MAX_SIZE = 10;
    /** Rocksdb库对象 */
    protected RocksDbStore store;
    /** 政策拉取参数包装类 */
    protected PolicySearchWrapper searchWrapper;
    /** 回调事件集 */
    private final Collection<PolicyEvent> events;

    public AbstractPolicySync(RocksDbStore store, PolicySearchWrapper searchWrapper) {
        this.store = store;
        this.searchWrapper = searchWrapper;
        this.events = searchWrapper.getEvents();
    }

    /**
     * 启动拉取政策数据定时任务
     * @param searchWrapper
     * @param executorService
     */
    protected void search(PolicySearchWrapper searchWrapper, ScheduledExecutorService executorService) {
        PolicySearchWrapper.ProcessorStatePair statePair = searchWrapper.getStatePair();
        executorService.scheduleWithFixedDelay(() -> {
            while (true) {
                try {
                    if (isFullSyncState()) {
                        statePair.setPolicyStatus(PolicySyncStatusEnum.STOP);
                        log.info("stop search policy for full sync state,the policy type is {}.", searchWrapper.getPolicySyncTypeName());
                        break;
                    }
                    // 查询同步系统拉取数据
                    byte[] bytes = PolicySyncClient.search(searchWrapper);
                    // 刷新数据和回调通知 & 识别是否已拉取完
                    if (!flushAndCallBack(searchWrapper, bytes)) {
                        // 当前时间间隔，数据已拉取完毕，终止自旋
                        statePair.setPolicyStatus(PolicySyncStatusEnum.READY);
                        break;
                    }
                } catch (Throwable e) {
                    log.error("[{}]policy sync happen exception: the revision is {}", e, searchWrapper.getPolicySyncTypeName(), statePair.getRevision());
                    recordError(searchWrapper, e);
                    break;
                }
            }
            if (Objects.nonNull(searchWrapper.getPolicyConfig().getCallbackWrapper())) {
                searchWrapper.getPolicyConfig().getCallbackWrapper().getSyncNotice().accept(searchWrapper);
            }
            statePair.setLastSearchTime(new Date());
        }, 0, searchWrapper.getPolicyConfig().getUpdateInterval(), TimeUnit.MILLISECONDS);
        statePair.setStarted(true);
        statePair.setStartTime(System.currentTimeMillis());
    }

    /**
     * policy config 中 fullSyncState 是否为 true
     * @return boolean
     */
    protected boolean isFullSyncState() {
        return BooleanUtils.isTrue(searchWrapper.getPolicyConfig().isFullSyncState());
    }

    /**
     * 启动拉取上报版本定时任务
     * @param searchWrapper
     * @param executorService
     */
    protected void uploadRevision(PolicySearchWrapper searchWrapper, ScheduledExecutorService executorService) {
        executorService.scheduleWithFixedDelay(() -> {
            PolicySearchWrapper.ProcessorStatePair statePair = searchWrapper.getStatePair();
            PolicySyncTypeEnum policySyncType = searchWrapper.getPolicySyncType();
            if (isFullSyncState()) {
                statePair.setUploadRevisionStatus(PolicySyncStatusEnum.STOP);
                LoggerUtils.info(log, "stop upload revision for full sync state,the policy type is {}.", policySyncType.name());
                return;
            }
            try {
                PolicySyncClient.upload(searchWrapper, store.getKeyNumWithInCompactRange(policySyncType.getName()), searchWrapper.getQueue().size());
            } catch (Throwable e) {
                LoggerUtils.error(log, "[{}]政策上传同步进度异常：", e, policySyncType.name());
                recordError(searchWrapper, e);
            } finally {
                statePair.setUploadRevisionStatus(PolicySyncStatusEnum.READY);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * 触发回调事件
     * @param searchWrapper
     * @param policy
     * @param dataStatus
     */
    protected abstract void callBack(PolicySearchWrapper searchWrapper, BaseModel policy, PolicyUpdateTypeEnum dataStatus);

    /**
     * 执行回调事件
     * @param uploadWrapper
     * @param callBackEventExecutor
     */
    protected void doCallback(CallBackEventWrapper uploadWrapper, ExecutorService callBackEventExecutor) {
        for (PolicyEvent event : events) {
            callBackEventExecutor.submit(() -> event.getConsumer().accept(uploadWrapper));
        }
    }

    /**
     * 刷新数据和回调通知
     * byte数组数据格式
     * -----8--------|------4-------|--bytesLength--|-----4--------|--bytesLength--|.......
     * 整个list的长度 |第一个item的长度|第一个item的内容|第二个item的长度|第二个item的内容.........
     * @param searchWrapper
     * @param bytes
     * @return true-拉取有数据
     * @throws Exception
     */
    private boolean flushAndCallBack(PolicySearchWrapper searchWrapper, byte[] bytes) throws Exception {
        searchWrapper.getStatePair().setLastSearchTime(new Date());
        PolicySyncTypeEnum policySyncType = searchWrapper.getPolicySyncType();
        PolicyConfig policyConfig = searchWrapper.getPolicyConfig();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int size = getByteBufferSize(byteBuffer);
        LoggerUtils.debug(policyConfig, log, "[{}]search policy success: byte length {},data size {}", policySyncType.name(), bytes.length, size);
        for (int i = 0; i < size; i++) {
            // 解析数据 & 反序列化为政策对象
            BaseModel policy = getPolicy(searchWrapper, byteBuffer);
            log.info("policy:{}", JSON.toJSONString(policy));
            // 数据持久化到rocksdb
            PolicyUpdateTypeEnum dataStatus = flushRocksdb(searchWrapper, i, policy, (IndexKey) policy);
            // 触发回调事件
            callBack(searchWrapper, policy, dataStatus);
            // 刷新最新版本号
            syncRevision(searchWrapper, policy);
        }
        return size > 0;
    }

    /**
     * 获取政策对象(反序列化)
     * @param searchWrapper
     * @param byteBuffer
     * @return
     */
    private BaseModel getPolicy(PolicySearchWrapper searchWrapper, ByteBuffer byteBuffer) {
        PolicyConfig policyConfig = searchWrapper.getPolicyConfig();
        byte[] batchBytes = getBytes(byteBuffer);
        BaseModel policy = BaseModel.class
            .cast(SerializerUtils.deSerializerAndUnCompress(policyConfig.getSerializerType(), batchBytes, searchWrapper.getPolicySyncType().getClazz()));
        policy.setGmtLanding(new Date());
        return policy;
    }

    /**
     * 数据持久化到rocksdb
     * @param searchWrapper
     * @param i
     * @param policy
     * @param key
     * @return
     * @throws Exception
     */
    private PolicyUpdateTypeEnum flushRocksdb(PolicySearchWrapper searchWrapper, int i, BaseModel policy, IndexKey key) throws Exception {
        PolicySyncTypeEnum policySyncType = searchWrapper.getPolicySyncType();
        PolicyConfig policyConfig = searchWrapper.getPolicyConfig();
        PolicyUpdateTypeEnum updateType = getPolicyModelStatus(policy);
        switch (updateType) {
            case DELETE:
                store.delete(policySyncType.getName(), key.getIndexKeyBytes());
                LoggerUtils.info(log, "[{}]第{}条,删除数据,元数据:{}", policySyncType.getName(), i, key.getIndexKey());
                LoggerUtils.debug(policyConfig, log, "[{}]第{}条,删除数据,元数据:{}", policySyncType.getName(), i, policy);
                break;
            case CREATE:
            case UPDATE:
                store.put(policySyncType.getName(), key.getIndexKeyBytes(), SerializerUtils.serializer(policyConfig.getSerializerType(), policy), false);
                LoggerUtils.debug(policyConfig, log, "[{}]第{}条,更新数据,元数据:{}", policySyncType.getName(), i, policy);
                break;
        }
        return updateType;
    }

    /**
     * 刷新最新版本号
     * @param searchWrapper
     * @param policy
     * @throws Exception
     */
    private void syncRevision(PolicySearchWrapper searchWrapper, BaseModel policy) throws Exception {
        PolicySearchWrapper.ProcessorStatePair statePair = searchWrapper.getStatePair();
        // 更新最大版本号 & 更新rocksdb列族
        if (policy.getRevision() > statePair.getRevision()) {
            statePair.setRevision(policy.getRevision());
            store.put(PolicySyncConstants.COLUMN_POLICY_VERSION, searchWrapper.getKeyBytes(), ProtocolConverter.long2Bytes(statePair.getRevision()), false);
            LoggerUtils.debug(searchWrapper.getPolicyConfig(), log, "政策类型:{},更新最大版本号:{}", searchWrapper.getPolicySyncTypeName(), statePair.getRevision());
        }
        if (StringUtils.isBlank(policy.getTaskId())) {
            return;
        }
        // 更新批次号对应最大批次号，用于上报
        long revision = statePair.getRevisions().getOrDefault(policy.getTaskId(), 0L);
        if (policy.getRevision() > revision) {
            statePair.getRevisions().put(policy.getTaskId(), policy.getRevision());
        }
    }

    /**
     * 识别数据状态
     * @param policy
     * @return CREATE OR UPDATE OR DELETE
     */
    private PolicyUpdateTypeEnum getPolicyModelStatus(BaseModel policy) {
        if (policy.getIsDelete() == NumberUtils.INTEGER_ONE.intValue()) {
            return PolicyUpdateTypeEnum.DELETE;
        }
        if (policy.getStatus() == NumberUtils.INTEGER_ONE.intValue()) {
            if (policy instanceof BasePolicyDO) {
                if (BasePolicyDO.class.cast(policy).getVersion() == NumberUtils.INTEGER_ZERO.intValue()) {
                    return PolicyUpdateTypeEnum.CREATE;
                }
            }
            return PolicyUpdateTypeEnum.UPDATE;
        }
        return PolicyUpdateTypeEnum.DELETE;
    }

    /**
     * 从ByteBuffer读取数据写入byte数组
     * @param byteBuffer
     * @return
     */
    private byte[] getBytes(ByteBuffer byteBuffer) {
        int size = getByteBufferSize(byteBuffer);
        byte[] bytes = new byte[size];
        byteBuffer.get(bytes, 0, size);
        return bytes;
    }

    /**
     * 获取ByteBuffer缓存byte字节数组长度
     * @param byteBuffer
     * @return
     */
    private int getByteBufferSize(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[4];
        byteBuffer.get(bytes, 0, 4);
        return ProtocolConverter.bytes2Int(bytes);
    }

    /**
     * 记录同步异常数据
     * @param searchWrapper
     * @param throwable
     */
    private void recordError(PolicySearchWrapper searchWrapper, Throwable throwable) {
        String message = throwable.getMessage() + "\r\n" + ExceptionUtils.getStackTrace(throwable);
        Collection<ErrorRecord> errorRecords = searchWrapper.getStatePair().getErrorRecords();
        errorRecords.add(new ErrorRecord(message, System.currentTimeMillis()));
        while (errorRecords.size() > EVENT_MAX_SIZE) {
            errorRecords.remove(NumberUtils.INTEGER_ZERO);
        }
    }

    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    @Override
    public void executorShutdown() {
        // no-op
    }

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    @Override
    public boolean isExecutorTerminated() {
        return true;
    }

    /**
     * 关闭数据库
     */
    @Override
    public void close() {
        // no-op
    }

    /**
     * 替换 store
     * @param store RocksDbStore
     */
    @Override
    public void replaceStore(RocksDbStore store) {
        this.store = store;
    }

    /**
     * Gets revision.
     *
     * @param keyBytes the key bytes
     * @return the revision
     * @throws Exception the exception
     */
    Long getRevision(byte[] keyBytes) {
        try {
            byte[] bytes = store.get(PolicySyncConstants.COLUMN_POLICY_VERSION, keyBytes);
            return (bytes != null && bytes.length > 0) ? ProtocolConverter.bytes2Long(bytes) : 0L;
        } catch (Exception e) {
            LoggerUtils.error(log, "Get {} policy version happen exception of group key is {}", e, searchWrapper.getPolicySyncTypeName(), new String(keyBytes));
        }
        return 0L;
    }
}
