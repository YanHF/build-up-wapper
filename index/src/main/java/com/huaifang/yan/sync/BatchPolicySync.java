package com.huaifang.yan.sync;

import com.huaifang.yan.enums.PolicyOperateSourceEnum;
import com.huaifang.yan.enums.PolicySyncStatusEnum;
import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.huaifang.yan.model.CallBackEventWrapper;
import com.huaifang.yan.model.ExecutorPair;
import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbStore;
import com.huaifang.yan.utils.LoggerUtils;
import com.huaifang.yan.utils.SerializerUtils;
import com.ly.flight.xpgs.base.constant.Symbols;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.base.model.BaseModel;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * 批量模式政策同步
 *
 * @author xianyan.geng
 * @version BatchPolicySync, v 0.1 2020/10/21 11:35 xianyan.geng Exp $
 */
@Slf4j
public class BatchPolicySync extends AbstractPolicySync {

    /** 线程池Pair Key：操作来源 */
    private final Map<String, ExecutorPair> executorPairs;
    /** 所有分组的PolicySearchWrapper Key：操作来源 */
    private Map<String, PolicySearchWrapper> searchWrappers = new ConcurrentHashMap<>();

    public BatchPolicySync(RocksDbStore store, PolicySearchWrapper searchWrapper) {
        super(store, searchWrapper);
        this.executorPairs = new LinkedHashMap<>();
        for (PolicyOperateSourceEnum operateSource : PolicyOperateSourceEnum.values()) {
            this.executorPairs.put(operateSource.name(), new ExecutorPair(searchWrapper.getPolicyConfig().getThreadSize()));
        }
    }

    @Override
    public void start() {
        Map<String, PolicySearchWrapper> searchWrappers = new ConcurrentHashMap<>(executorPairs.size());
        for (Map.Entry<String, ExecutorPair> entry : executorPairs.entrySet()) {
            ExecutorPair executorPair = entry.getValue();
            Long revision = getStartRevision(searchWrapper.getPolicySyncType(), entry.getKey());
            PolicySearchWrapper wrapper = searchWrapper.copy(entry.getKey(), executorPair.getQueue(), entry.getKey(), revision);
            searchWrappers.put(entry.getKey(), wrapper);
            //  启动拉取政策定时任务
            search(wrapper, executorPair.getSearchPolicyExecutor());
            // 启动上报版本定时任务
            uploadRevision(wrapper, executorPair.getUploadRevisionExecutor());
        }
        this.searchWrappers = searchWrappers;
    }

    /**
     * Replace state pair.
     *
     * @param statePair the state pair
     */
    @Override
    public void updateStatePair(PolicySearchWrapper.ProcessorStatePair statePair) {
        for (PolicySearchWrapper searchWrapper : searchWrappers.values()) {
            searchWrapper.getStatePair().setRevision(statePair.getRevision());
        }
    }

    /**
     * 政策同步/上报 任务是否已暂停
     *
     * @return the boolean
     */
    @Override
    public boolean isSyncStop() {
        if (!isFullSyncState()) {
            return false;
        }
        for (PolicySearchWrapper wrapper : searchWrappers.values()) {
            PolicySearchWrapper.ProcessorStatePair statePair = wrapper.getStatePair();
            if (statePair.getPolicyStatus() != PolicySyncStatusEnum.STOP || statePair.getUploadRevisionStatus() != PolicySyncStatusEnum.STOP) {
                LoggerUtils.info(log, "policy can not stop:{},syncStatus:{} uploadStatus:{} ", wrapper.getPolicySyncTypeName(), statePair.getPolicyStatus(),
                        statePair.getUploadRevisionStatus());
                return false;
            }
        }
        return true;
    }

    @Override
    protected void callBack(PolicySearchWrapper searchWrapper, BaseModel policy, PolicyUpdateTypeEnum dataStatus) {
        ExecutorService callBackEventExecutor = executorPairs.get(searchWrapper.getGroupKey()).getCallBackEventExecutor();
        doCallback(new CallBackEventWrapper<>(searchWrapper.getPolicySyncType(), dataStatus, policy), callBackEventExecutor);
    }

    /**
     * 获取分组当前拉取Revision
     *
     * @param policySyncType the policy sync type
     * @param groupKey       the group key
     * @return the byte [ ]
     */
    private byte[] buildPolicyRevisionColumn(PolicySyncTypeEnum policySyncType, String groupKey) {
        StringJoiner stringJoiner = new StringJoiner(Symbols.UNDERLINE).add(policySyncType.name()).add(groupKey);
        return stringJoiner.toString().getBytes(SerializerUtils.DEFAULT_CHARSET);
    }

    /**
     * 获取起始Revision
     *
     * @param policySyncType the policy sync type
     * @param groupKey       the group key
     * @return the start revision
     */
    private Long getStartRevision(PolicySyncTypeEnum policySyncType, String groupKey) {
        return getRevision(buildPolicyRevisionColumn(policySyncType, groupKey));
    }
}
