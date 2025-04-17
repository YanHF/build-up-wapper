package com.huaifang.yan;


import com.huaifang.yan.exeption.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ConsumerController {
    private static final BusinessExceptionErrorFactory  errorFactory =new BusinessExceptionErrorFactory();

    @RequestMapping("/consumer")
    public String index() {


        try {
            throw new BusinessException(errorFactory.systemError("A"));
        }
        catch (Exception e) {
            log.error("请求失败",e);
        }

        return null;
    }

    @RequestMapping("/add")
    public String add() {

        return null;
    }

}
