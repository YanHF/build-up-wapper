package com.huaifang.yan.model;

import com.huaifang.yan.enums.PolicyUpdateTypeEnum;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;

import lombok.Getter;

import java.util.Collection;

/**
 * 回调事件包装类
 *
 * @author xianyan.geng
 * @version CallBackEventWrapper, v 0.1 2020/10/20 13:46 xianyan.geng Exp $
 */
@Getter
public class CallBackEventWrapper<T> {
    /** 政策同步类型 */
    private final PolicySyncTypeEnum   policySyncType;
    /** 政策修改类型 */
    private final PolicyUpdateTypeEnum updateType;
    /** 单条政策数据 */
    private T                          data;
    /** 全量政策数据 */
    private Collection<T>              fullData;

    public CallBackEventWrapper(PolicySyncTypeEnum policySyncType, PolicyUpdateTypeEnum updateType, T data) {

        this.policySyncType = policySyncType;
        this.updateType = updateType;
        this.data = data;
    }

    public CallBackEventWrapper(PolicySyncTypeEnum policySyncType, PolicyUpdateTypeEnum updateType, Collection<T> fullData) {
        this.policySyncType = policySyncType;
        this.updateType = updateType;
        this.fullData = fullData;
    }
}
