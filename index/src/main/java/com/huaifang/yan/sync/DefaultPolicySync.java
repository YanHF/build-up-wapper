package com.huaifang.yan.sync;

import com.huaifang.yan.enums.PolicySyncStatusEnum;
import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.huaifang.yan.model.CallBackEventWrapper;
import com.huaifang.yan.model.ExecutorPair;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbStore;
import com.huaifang.yan.utils.LoggerUtils;
import com.ly.flight.xpgs.base.model.BaseModel;

import lombok.extern.slf4j.Slf4j;

/**
 * 正常模式政策同步
 *
 * @author xianyan.geng
 * @version DefaultPolicySync, v 0.1 2020/10/20 13:44 xianyan.geng Exp $
 */
@Slf4j
public class DefaultPolicySync extends AbstractPolicySync {

    /** 回调事件线程池 */
    private final ExecutorPair executorPair;

    public DefaultPolicySync(RocksDbStore store, PolicySearchWrapper searchWrapper) {
        super(store, searchWrapper);
        this.executorPair = new ExecutorPair(searchWrapper.getPolicyConfig().getThreadSize());
    }

    @Override
    public void start() {
        searchWrapper.setQueue(executorPair.getQueue());
        //  启动拉取政策定时任务
        search(searchWrapper, executorPair.getSearchPolicyExecutor());
        // 启动上报版本定时任务
        uploadRevision(searchWrapper, executorPair.getUploadRevisionExecutor());
    }


    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    @Override
    public void executorShutdown() {
        executorPair.getSearchPolicyExecutor().shutdown();
        executorPair.getUploadRevisionExecutor().shutdown();
        executorPair.getCallBackEventExecutor().shutdown();
    }

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    @Override
    public boolean isExecutorTerminated() {
        return executorPair.getSearchPolicyExecutor().isTerminated() && executorPair.getUploadRevisionExecutor().isTerminated()
                && executorPair.getCallBackEventExecutor().isTerminated();
    }

    /**
     * 关闭数据库
     */
    @Override
    public void close() {
        store.close();
    }

    /**
     * Replace state pair.
     *
     * @param statePair the state pair
     */
    @Override
    public void updateStatePair(PolicySearchWrapper.ProcessorStatePair statePair) {
        searchWrapper.getStatePair().setRevision(statePair.getRevision());
    }

    /**
     * 政策同步/上报 任务是否已暂停
     *
     * @return the boolean
     */
    @Override
    public boolean isSyncStop() {
        PolicySearchWrapper.ProcessorStatePair statePair = searchWrapper.getStatePair();
        boolean result = isFullSyncState() && statePair.getPolicyStatus() == PolicySyncStatusEnum.STOP && statePair.getUploadRevisionStatus() == PolicySyncStatusEnum.STOP;
        if (!result) {
            LoggerUtils.info(log, "policy can not stop:{}, fullState:{} syncStatus:{} uploadStatus:{}", searchWrapper.getPolicySyncTypeName(), isFullSyncState(),
                    statePair.getPolicyStatus(), statePair.getUploadRevisionStatus());
        }
        return result;
    }

    @Override
    protected void callBack(PolicySearchWrapper searchWrapper, BaseModel policy, PolicyUpdateTypeEnum dataStatus) {
        doCallback(new CallBackEventWrapper<>(searchWrapper.getPolicySyncType(), dataStatus, policy), executorPair.getCallBackEventExecutor());
    }
}
