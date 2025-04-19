package com.huaifang.yan.model;

import com.huaifang.yan.OrderStates;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private OrderStates orderState;

}
