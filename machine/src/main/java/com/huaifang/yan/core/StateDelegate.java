/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.exeption.BusinessException;

import java.util.Map;

/**
 * 状态委托代理
 * 
 * @author allen
 * @version $Id: StateDelegate.java, v 0.1 2017年9月3日 下午10:30:52 allen Exp $
 */
public interface StateDelegate<Model, MBiz, MState> {

    /**
     * 业务状态驱动
     * 
     * @param model         业务数据
     * @param toBiz         目标业务
     * @param toState       目标状态
     * @param properties    扩展属性
     * @throws BusinessException 状态处理异常
     */
    void setState(Model model, MBiz toBiz, MState toState, Map<String, Object> properties) throws BusinessException;

    /**
     * 业务状态驱动
     * 
     * @param model 业务数据
     * @param toState 目标状态
     * @throws BusinessException 状态处理异常
     */
    void setState(Model model, MState toState) throws BusinessException;

    /**
     * 业务状态驱动
     *
     * @param model         业务数据
     * @param toBiz         目标业务
     * @param toState       目标状态
     * @param properties    扩展属性
     * @throws BusinessException 状态处理异常
     */
    void setState(Model model, MBiz toBiz, MState toState, boolean isStateValidate, Map<String, Object> properties) throws BusinessException;

    /**
     * 业务状态驱动
     *
     * @param model 业务数据
     * @param toState 目标状态
     * @throws BusinessException 状态处理异常
     */
    void setState(Model model, MState toState, boolean isStateValidate) throws BusinessException;
}
