/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.event;


import com.huaifang.yan.annotation.State;

/**
 * Actor注册校验器
 * 
 * @author allen
 * @version $Id: ActorRegistryValidator.java, v 0.1 2017年9月3日 下午11:34:26 allen Exp $
 */
public interface ActorRegistryValidator {

    /**
     * Annotation value validation
     * 
     * @param state
     * @return 状态是否有效
     */
    boolean validate(State state);
}
