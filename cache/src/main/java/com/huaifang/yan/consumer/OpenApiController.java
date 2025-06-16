package com.huaifang.yan.consumer;

import com.huaifang.yan.event.CouponEvent;
import com.huaifang.yan.event.RequestEvent;
import com.huaifang.yan.feign.ConsumerApi;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.Computable;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class OpenApiController {


    @RequestMapping("/notice")
    public String notice() {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate.getForObject("http://localhost:8080/consumer", String.class);
    }


    @RequestMapping("/netty")
    public String netty() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup, eventLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);

        return "netty";
    }
}
