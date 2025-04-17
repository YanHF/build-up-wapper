package com.huaifang.yan.productor;

import com.huaifang.yan.event.RequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @EventListener// <1>

    public void addCoupon(RequestEvent event) {

        logger.info("给用户({}) 发放优惠劵", event.getPublishName());

    }
}
