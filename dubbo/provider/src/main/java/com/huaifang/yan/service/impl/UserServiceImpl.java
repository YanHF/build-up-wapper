package com.huaifang.yan.service.impl;


import com.huaifang.yan.facade.IUserService;
import com.huaifang.yan.model.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

// 定义一个Dubbo服务
@Slf4j
@DubboService
public class UserServiceImpl  implements IUserService {
    @Override
    public UserVo getUserById(Long id) {
        log.info("获取用户信息 userId:{}",id);
        UserVo user = UserVo.builder().id(id)
                .age(12)
                .name("天涯")
                .build();
        return user;
    }
}
