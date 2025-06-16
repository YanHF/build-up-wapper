package com.huaifang.yan.controller;

import com.huaifang.yan.facade.IUserService;
import com.huaifang.yan.model.UserVo;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.SocketChannel;

@RestController
public class ConsumerController {

    @DubboReference
    private IUserService iUserService;

    @RequestMapping("/consumer")
    public String consumer(){
        UserVo vo= iUserService.getUserById(0L);
        return vo.toString();
    }
}
