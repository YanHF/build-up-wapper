package com.huaifang.yan.model;

import com.alibaba.fastjson.JSON;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 政策上传版本模型
 *
 * @author xianyan.geng
 * @version PolicyMonitor, v 0.1 2020/10/20 13:46 xianyan.geng Exp $
 */
public class PolicyMonitor implements Serializable {
    private static final long serialVersionUID = -3441065705346790550L;
    /** 政策类型 */
    private PolicySyncTypeEnum policySyncType;
    /** 是否已启动拉取政策 */
    private boolean            started;
    /** 启动时间戳 */
    private long               startTime;
    /** 本次最大版本号 */
    private long               revision;
    /** 批次号对应最大版本号数据 Key：批次号 Val：最大版本号 */
    private Map<String, Long>  revisions;
    /** Rocksdb数据条数 */
    private long               size;
    /** 最后拉取时间戳 */
    private long               updateTime;
    /** 回调通知排队数量 */
    private long               eventSize;
    /** 处理失败记录 */
    private List<ErrorRecord>  records;
    /** 拉取模式：正常模式、批量模式、VIP模式 */
    private String             searchType;
    /** 拉取参数 拉取类型为0：空，拉取类型为1：WEB/EXCEL/API，拉取类型为2：供应商编号/OTHER */
    private String             searchValue;
    /** 拉取分类值，多个逗号隔开(批量模式和VIP模式必传) */
    private String             searchAllValue;
    /** docker实例环境标示 */
    private String             env;

    /**
     * Gets policy sync type.
     *
     * @return the policy sync type
     */
    public PolicySyncTypeEnum getPolicySyncType() {
        return policySyncType;
    }

    /**
     * Sets policy sync type.
     *
     * @param policySyncType the policy sync type
     */
    public void setPolicySyncType(PolicySyncTypeEnum policySyncType) {
        this.policySyncType = policySyncType;
    }

    /**
     * Is started boolean.
     *
     * @return the boolean
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Sets started.
     *
     * @param started the started
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets revision.
     *
     * @return the revision
     */
    public long getRevision() {
        return revision;
    }

    /**
     * Sets revision.
     *
     * @param revision the revision
     */
    public void setRevision(long revision) {
        this.revision = revision;
    }

    /**
     * Gets revisions.
     *
     * @return the revisions
     */
    public Map<String, Long> getRevisions() {
        return revisions;
    }

    /**
     * Sets revisions.
     *
     * @param revisions the revisions
     */
    public void setRevisions(Map<String, Long> revisions) {
        this.revisions = revisions;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Gets update time.
     *
     * @return the update time
     */
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets update time.
     *
     * @param updateTime the update time
     */
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Gets event size.
     *
     * @return the event size
     */
    public long getEventSize() {
        return eventSize;
    }

    /**
     * Sets event size.
     *
     * @param eventSize the event size
     */
    public void setEventSize(long eventSize) {
        this.eventSize = eventSize;
    }

    /**
     * Gets records.
     *
     * @return the records
     */
    public List<ErrorRecord> getRecords() {
        return records;
    }

    /**
     * Sets records.
     *
     * @param records the records
     */
    public void setRecords(List<ErrorRecord> records) {
        this.records = records;
    }

    /**
     * Gets search type.
     *
     * @return the search type
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * Sets search type.
     *
     * @param searchType the search type
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * Gets search value.
     *
     * @return the search value
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * Sets search value.
     *
     * @param searchValue the search value
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    /**
     * Gets search all value.
     *
     * @return the search all value
     */
    public String getSearchAllValue() {
        return searchAllValue;
    }

    /**
     * Sets search all value.
     *
     * @param searchAllValue the search all value
     */
    public void setSearchAllValue(String searchAllValue) {
        this.searchAllValue = searchAllValue;
    }

    /**
     * Gets env.
     *
     * @return the env
     */
    public String getEnv() {
        return env;
    }

    /**
     * Sets env.
     *
     * @param env the env
     */
    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
