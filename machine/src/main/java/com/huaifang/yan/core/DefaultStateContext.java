/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.exeption.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认状态上下文实现
 * 
 * @author allen
 * @version $Id: DefaultStateContext.java, v 0.1 2017年9月3日 下午8:25:12 allen Exp $
 */
public class DefaultStateContext<T, BizType, StateValue> implements StateContext<T, BizType, StateValue> {

    /** 状态上下文ID */
    private String                         id;

    /** 当前状态 */
    private IState<T, BizType, StateValue> fromState;

    /** 流转状态 */
    private IState<T, BizType, StateValue> toState;

    /** 业务数据 */
    private T                              data;

    /** 上下文属性 */
    private Map<String, Object>            properties = new HashMap<>();
    /**
     * 状态流转是否check原状态
     */
    private boolean                        stateValidate;

    /**
     * 
     * @param id 上下文ID
     * @param data 业务数据
     */
    public DefaultStateContext(String id, T data) {
        super();
        this.id = id;
        this.data = data;
    }

    /**
     *
     * @param id 上下文ID
     * @param data 业务数据
     */
    public DefaultStateContext(String id, boolean stateValidate, T data) {
        super();
        this.id = id;
        this.data = data;
        this.stateValidate = stateValidate;
    }

    /**
     * @see StateContext#setState(IState, IState)
     */
    @Override
    public void setState(IState<T, BizType, StateValue> from, IState<T, BizType, StateValue> to) throws BusinessException
    {
        this.fromState = from;
        this.toState = to;
        toState.handle(this);
    }

    /** 
     * @see StateContext#getFromBiz()
     */
    @Override
    public BizType getFromBiz() {
        return fromState.getBizType();
    }

    /** 
     * @see StateContext#getToBiz()
     */
    @Override
    public BizType getToBiz() {
        return toState.getBizType();
    }

    /** 
     * @see StateContext#getFromState()
     */
    @Override
    public IState<T, BizType, StateValue> getFromState() {
        return fromState;
    }

    /** 
     * @see StateContext#getToState()
     */
    @Override
    public IState<T, BizType, StateValue> getToState() {
        return toState;
    }

    /**
     * @see StateContext#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @see StateContext#getData()
     */
    @Override
    public T getData() {
        return data;
    }

    /**
     * @see StateContext#getProperties()
     */
    @Override
    public Map<String, Object> getProperties() {
        if (properties.isEmpty()) {
            properties = new HashMap<>();
        }
        return properties;
    }

    /**
     * @see StateContext#addProperty(String, Object)
     */
    @Override
    public void addProperty(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        getProperties().put(key, value);
    }

    @Override
    public boolean isStateValidate() {
        return stateValidate;
    }
}
