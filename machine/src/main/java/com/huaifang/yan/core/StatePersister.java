/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.exeption.BusinessException;

/**
 * 状态持久化
 * 
 * @author allen
 * @version $Id: StatePersister.java, v 0.1 2017年9月3日 下午6:59:59 allen Exp $
 */
public interface StatePersister<T, BizType, StateValue> {

    /**
     * 状态持久化，更新状态
     * 1、持久化状态变更
     * 2、回写业务数据模型
     * 
     * @param data 业务数据
     * @param toBiz 目标业务
     * @param toState 目标状态
     * @throws BusinessException 状态持久化异常
     */
    void updateState(T data, BizType toBiz, StateValue toState) throws BusinessException;

    /**
     * 支持对原有状态的check
     * @param data
     * @param fromBiz
     * @param fromState
     * @param toBiz
     * @param toState
     * @throws BusinessException
     */
    void updateState(T data, BizType fromBiz, StateValue fromState, BizType toBiz, StateValue toState) throws BusinessException;
}
