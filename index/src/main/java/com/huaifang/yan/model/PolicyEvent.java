package com.huaifang.yan.model;

import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.policy.model.IndexKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * 政策同步注册回调事件模型
 * @author xianyan.geng
 * @version PolicyEvent, v 0.1 2020/10/20 11:51 xianyan.geng Exp $
 */
@Getter
@AllArgsConstructor
public class PolicyEvent<T extends IndexKey> {
    /** 政策同步类型 */
    private final PolicySyncTypeEnum                policySyncType;
    /** 回调函数 */
    private final Consumer<CallBackEventWrapper<T>> consumer;
}
