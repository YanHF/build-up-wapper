package com.huaifang.yan.model;

import com.huaifang.yan.enums.PolicySyncModelEnum;
import com.huaifang.yan.enums.SerializerTypeEnum;
import com.ly.flight.xpgs.base.context.IndexFilterWrapperVO;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 政策配置模型
 * @author xianyan.geng
 * @version PolicyConfig, v 0.1 2020/10/20 11:51 xianyan.geng Exp $
 */
@Data
public class PolicyConfig {
    /** 政策类型名称 */
    private String             name;
    /** rocksdb文件目录 */
    private String             path           = "./policy";
    /** 政策同步系统接口地址前缀 */
    private String             backgroundUrl  = "http://pgs.sync.qa.ie.17usoft.com/policy-sync/";
    /** 拉取模式：正常模式、批量模式、VIP模式 */
    private String             searchType     = PolicySyncModelEnum.NORMAL.name();
    /** 拉取分类值，多个逗号隔开(批量模式和VIP模式必传) */
    private String             searchValue;
    /** 拉取间隔时间，单位：毫秒 */
    private int                updateInterval = 5000;
    /** 线程池数量 */
    private int                threadSize     = 1;
    /** 是否开启debug日志打印 true-开启 */
    private boolean            debug          = false;
    /** docker实例环境标示 */
    private String             env            = "test";
    /** 序列化类型 */
    private SerializerTypeEnum serializerType = SerializerTypeEnum.JSON;
    /** 政策同步类型 */
    private PolicySyncTypeEnum policySyncType;
    /** 调试日志采集函数包装类 */
    private FunctionWrapper    functionWrapper;

    /** 是否全量同步状态标记 true-是 */
    private volatile boolean                      fullSyncState;

    /** 全量同步回调参数 */
    private FullSyncCallbackWrapper               callbackWrapper;

    @Data
    public static class FunctionWrapper {
        /** 是否启用索引过滤数据采集 */
        private Supplier<Boolean>              isVector;
        /** 索引过滤数据采集器 */
        private Consumer<IndexFilterWrapperVO> filterVector;

    }

    @Data
    @NoArgsConstructor
    public static class FullSyncCallbackWrapper implements Serializable {
        private static final long             serialVersionUID = -23928940039983563L;
        /** traceId */
        private String                        traceId;
        /** dockerId */
        private String                        dockerId;
        /** 同步通知事件 */
        private Consumer<PolicySearchWrapper> syncNotice;

        public FullSyncCallbackWrapper(String traceId, String dockerId, Consumer<PolicySearchWrapper> syncNotice) {
            this.traceId = traceId;
            this.dockerId = dockerId;
            this.syncNotice = syncNotice;
        }

    }

}
