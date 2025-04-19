package com.huaifang.yan.sync;


import com.huaifang.yan.main.PolicySync;
import com.huaifang.yan.enums.PolicySyncModelEnum;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 政策同步代理类
 *
 * @author xianyan.geng
 * @version PolicySyncProxy, v 0.1 2020/10/20 20:33 xianyan.geng Exp $
 */
public class PolicySyncProxy implements PolicySync {
    /** 同步实现类 */
    private final Map<PolicySyncModelEnum, PolicySync> policySyncs;
    /** 政策拉取参数包装类 */
    private final PolicySearchWrapper searchWrapper;

    public PolicySyncProxy(RocksDbStore store, PolicySearchWrapper searchWrapper) {
        this.searchWrapper = searchWrapper;
        policySyncs = new HashMap<>(4);
        for (PolicySyncModelEnum syncModel : PolicySyncModelEnum.values()) {
            policySyncs.put(syncModel, syncModel.getBiFunction().apply(store, searchWrapper));
        }
    }

    @Override
    public void start() {
        policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).start();
    }

    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    @Override
    public void executorShutdown() {
        policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).executorShutdown();
    }

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    @Override
    public boolean isExecutorTerminated() {
        return policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).isExecutorTerminated();
    }

    /**
     * 关闭数据库
     */
    @Override
    public void close() {
        policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).close();
    }

    /**
     * 替换 store
     * @param store RocksDbStore
     */
    @Override
    public void replaceStore(RocksDbStore store) {
        policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).replaceStore(store);
    }

    @Override
    public void updateStatePair(PolicySearchWrapper.ProcessorStatePair statePair) {
        policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).updateStatePair(statePair);
    }

    @Override
    public boolean isSyncStop() {
        return policySyncs.get(PolicySyncModelEnum.getByName(searchWrapper.getPolicyConfig().getSearchType())).isSyncStop();
    }
}
