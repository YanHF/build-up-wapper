package com.huaifang.yan.annotation;

import com.huaifang.yan.OrderStates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })

public @interface State {
    /** 原始业务类型 */
    String fromBiz();

    /** 目标业务类型 */
    String toBiz();

    /** 原始业务的状态 */
    OrderStates[]  from();

    /** 目标的业务状态 */
    OrderStates[] to();

}
