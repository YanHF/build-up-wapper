package com.huaifang.yan.model;


import com.huaifang.yan.enums.PolicySyncStatusEnum;
import com.huaifang.yan.utils.SerializerUtils;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 政策拉取参数快照
 *
 * @author xianyan.geng
 * @version PolicySearchWrapper, v 0.1 2020/10/20 14:06 xianyan.geng Exp $
 */
@Data
public class PolicySearchWrapper implements Serializable {
    private static final long       serialVersionUID = -1983540176392144094L;

    /** 政策配置 */
    private PolicyConfig            policyConfig;
    /** 政策类型 */
    private PolicySyncTypeEnum policySyncType;
    /** 处理过程属性 */
    private ProcessorStatePair      statePair;
    /** 回调事件集 */
    private Collection<PolicyEvent> events;
    /** 分组Key */
    private String                  groupKey;
    /** 回调通知排队队列 */
    private Queue                   queue;
    /** Rocksdb列族Key */
    private transient byte[]        keyBytes;

    public PolicySearchWrapper(PolicyConfig policyConfig, PolicySyncTypeEnum policySyncType, ProcessorStatePair statePair, Collection<PolicyEvent> events) {
        this(policyConfig, policySyncType, statePair, events, null, null);
    }

    public PolicySearchWrapper(PolicyConfig policyConfig, PolicySyncTypeEnum policySyncType, ProcessorStatePair statePair, Collection<PolicyEvent> events, String groupKey,
                               Queue queue) {
        this.policyConfig = policyConfig;
        this.policySyncType = policySyncType;
        this.statePair = statePair;
        this.events = events;
        this.groupKey = groupKey;
        this.queue = queue;
    }

    /**
     * 获取政策类型名称
     * @return
     */
    public String getPolicySyncTypeName() {
        return policySyncType.name();
    }

    /**
     * 复制新查询快照
     * @param groupKey
     * @param queue
     * @param searchValue
     * @return
     */
    public PolicySearchWrapper copy(String groupKey, Queue queue, String searchValue, Long revision) {
        ProcessorStatePair source = getStatePair();
        ProcessorStatePair statePair = new ProcessorStatePair(source.getSearchType(), source.getRevision(), source.getPolicyStatus(), source.getLastSearchTime());
        statePair.setSearchValue(searchValue);
        statePair.setRevision(revision);
        statePair.setPolicyStatus(statePair.getRevision() > 0L ? PolicySyncStatusEnum.DELAY : PolicySyncStatusEnum.INIT);
        return new PolicySearchWrapper(getPolicyConfig(), getPolicySyncType(), statePair, new ArrayList<>(), groupKey, queue);
    }

    /**
     * 获取Rocksdb列族Key
     * @return
     */
    public byte[] getKeyBytes() {
        if (keyBytes == null) {
            keyBytes = policySyncType.name().getBytes(SerializerUtils.DEFAULT_CHARSET);
        }
        return keyBytes;
    }

    @Data
    @AllArgsConstructor
    public static class ProcessorStatePair {
        /** 拉取模式：正常模式、批量模式、VIP模式 */
        private String               searchType;
        /** 拉取参数 拉取类型为0：空，拉取类型为1：WEB/EXCEL/API，拉取类型为2：供应商编号/OTHER */
        private String               searchValue;
        /** 最大版本号 */
        private long                 revision;
        /** 批次号对应最大版本号数据 Key：批次号 Val：最大版本号 */
        private Map<String, Long>    revisions;
        /** 是否启动拉取，true-已启动 false-未启动 */
        private boolean              started;
        /** 启动拉取时间点 */
        private long                 startTime;
        /** 政策查询状态 */
        private PolicySyncStatusEnum policyStatus;
        /** 上传版本号状态 */
        private PolicySyncStatusEnum uploadRevisionStatus;

        /** 最后拉取时间 */
        private Date                 lastSearchTime;
        /** 全量/增量：0-增量(有效和无效) 1-全量(仅有效) */
        private int                  mode;
        /** 处理失败记录 */
        private List<ErrorRecord>    errorRecords;

        public ProcessorStatePair(String searchType, long revision, PolicySyncStatusEnum policyStatus, Date lastSearchTime) {
            this.searchType = searchType;
            this.searchValue = StringUtils.EMPTY;
            this.revision = revision;
            this.revisions = new ConcurrentHashMap<>();
            this.policyStatus = policyStatus;
            this.lastSearchTime = lastSearchTime;
            this.errorRecords = new CopyOnWriteArrayList<>();
        }

        public void release() {
            this.revisions.clear();
            this.errorRecords.clear();
        }
    }
}
