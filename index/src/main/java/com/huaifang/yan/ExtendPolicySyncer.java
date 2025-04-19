package com.huaifang.yan;

import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.huaifang.yan.model.CallBackEventWrapper;
import com.huaifang.yan.model.PolicyConfig;
import com.huaifang.yan.model.PolicyEvent;
import com.huaifang.yan.utils.LoggerUtils;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.policy.model.IndexKey;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 政策同步扩展处理器
 *
 * @author xianyan.geng
 * @version ExtendSyncer, v 0.1 2020/11/16 17:51 xianyan.geng Exp $
 */
@Slf4j
public class ExtendPolicySyncer<T extends IndexKey> implements PolicySyncer {

    /** 政策配置 */
    private final PolicyConfig policyConfig;
    /** 数据仓库 */
    private Map<String, T>                policies = new ConcurrentHashMap<>();
    /** 回调事件集 */
    private final Collection<PolicyEvent> events   = new ArrayList<>();
    /** 扩展数据提供者 */
    private final ExtendDataProvider      extendDataProvider;
    /** 回调事件线程池 */
    private final ExecutorService         callBackEventExecutor;

    public ExtendPolicySyncer(PolicyConfig policyConfig, ExtendDataProvider extendDataProvider) throws Exception {
        this.policyConfig = policyConfig;
        this.extendDataProvider = extendDataProvider;
        this.callBackEventExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    /**
     * 触发定时拉取政策任务
     */
    @Override
    public void start() {
        // 全量数据拉取
        Collection<T> sources = extendDataProvider.queryFullData();
        if (sources != null && sources.size() > 0) {
            sources.forEach(policy -> {
                policies.put(policy.getIndexKey(), policy);
                callback(new CallBackEventWrapper<>(policyConfig.getPolicySyncType(), PolicyUpdateTypeEnum.CREATE, policy));
            });
        }
        // 订阅增量回调通知事件
        extendDataProvider.addCallbackEvent(((key) -> dataChanged((CallBackEventWrapper<T>) key)));
        extendDataProvider.addFullCallbackEvent(((key) -> fullDataChanged((CallBackEventWrapper<T>) key)));
    }

    /**
     * 政策同步/上报 任务是否已暂停
     *
     * @return the boolean
     */
    @Override
    public boolean isSyncStop() {
        return false;
    }

    /**
     * 遍历数据
     *
     * @param biFunction
     */
    @Override
    public void forEach(Function biFunction) {
        policies.entrySet().forEach(entry -> biFunction.apply(entry.getValue()));
    }

    /**
     * 根据ID获取数据
     *
     * @param id
     * @return
     */
    @Override
    public T get(String id) {
        return policies.get(id);
    }

    /**
     * 获取指定政策类型Rocksdb对应的条数
     *
     * @return
     */
    @Override
    public long getRocksdbKeySize() {
        return policies.size();
    }

    /**
     * 追加回调事件
     *
     * @param consumer
     */
    @Override
    public void addCallbackEvent(Consumer consumer) {
        events.add(new PolicyEvent(policyConfig.getPolicySyncType(), consumer));
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        policies.clear();
        events.clear();
    }

    /**
     * 获取政策同步类型
     *
     * @return
     */
    @Override
    public PolicySyncTypeEnum getPolicySyncType() {
        return policyConfig.getPolicySyncType();
    }

    /**
     * Gets policy config.
     *
     * @return the policy config
     */
    @Override
    public PolicyConfig getPolicyConfig() {
        return policyConfig;
    }

    /**
     * 初始化 DB
     */
    @Override
    public void rebuild() throws Exception {

    }

    @Override
    public void init() throws Exception {

    }



    /**
     * 执行回调事件
     * @param eventWrapper
     */
    protected void callback(CallBackEventWrapper eventWrapper) {
        for (PolicyEvent event : events) {
            callBackEventExecutor.submit(() -> event.getConsumer().accept(eventWrapper));
        }
    }

    /**
     * 单条增量通知回调
     * @param eventWrapper
     */
    private void dataChanged(CallBackEventWrapper<T> eventWrapper) {
        T policy = eventWrapper.getData();
        switch (eventWrapper.getUpdateType()) {
            case DELETE:
                policies.remove(policy.getIndexKey());
                break;
            case CREATE:
            case UPDATE:
                policies.put(policy.getIndexKey(), policy);
                break;
            default:
                throw new IllegalStateException("Data change type is not supported:" + eventWrapper.getUpdateType().name());
        }
        callback(eventWrapper);
    }

    /**
     * 全量通知回调
     * @param eventWrapper
     */
    private void fullDataChanged(CallBackEventWrapper<T> eventWrapper) {
        switch (eventWrapper.getUpdateType()) {
            case CLEAR:
                // 清库
                LoggerUtils.warn(log, "接收到规则的全量数据为空，正在开始清理有效索引政策，请确认！", eventWrapper.getPolicySyncType().name());
                doCallback(policies.values(), PolicyUpdateTypeEnum.DELETE);
                policies.clear();
                break;
            case CREATE:
            case DELETE:
            case UPDATE:
                // 获取元数据
                Map<String, T> metaDataIndexKeys = getMetaDataIndexKeys(eventWrapper);
                // 查询待删除数据
                Set<T> delIndexKeys = getDelIndexKeys(metaDataIndexKeys);
                // 先变更数据
                doCallback(metaDataIndexKeys.values(), PolicyUpdateTypeEnum.UPDATE);
                // 再删除数据
                doCallback(delIndexKeys, PolicyUpdateTypeEnum.DELETE);
                // 刷新元数据
                policies = new ConcurrentHashMap<>(metaDataIndexKeys);
                break;
            default:
                throw new IllegalStateException("Data change type is not supported:" + eventWrapper.getUpdateType().name());
        }

    }

    /**
     * 获取元数据&转换
     * @param eventWrapper
     * @return
     */
    private Map<String, T> getMetaDataIndexKeys(CallBackEventWrapper<T> eventWrapper) {
        if(eventWrapper.getFullData() == null || eventWrapper.getFullData().size() <= 0){
            return new HashMap<>(1);
        }
        Map<String, T> indexKeys = new HashMap<>(32);
        eventWrapper.getFullData().forEach(policy -> indexKeys.put(policy.getIndexKey(), policy));
        return indexKeys;
    }

    /**
     * 查询待删除数据
     * @param metaDataIndexKeys
     * @return
     */
    private Set<T> getDelIndexKeys(Map<String, T> metaDataIndexKeys) {
        Set<T> delIndexKeys = new HashSet<>(32);
        policies.entrySet().forEach(entry -> {
            if (!metaDataIndexKeys.containsKey(entry.getKey())) {
                delIndexKeys.add(entry.getValue());
            }
        });
        return delIndexKeys;
    }

    /**
     * 批量回调通知
     * @param policies
     * @param updateType
     */
    private void doCallback(Collection<T> policies, PolicyUpdateTypeEnum updateType) {
        policies.forEach(policy -> callback(new CallBackEventWrapper<>(policyConfig.getPolicySyncType(), updateType, policy)));
    }
}
