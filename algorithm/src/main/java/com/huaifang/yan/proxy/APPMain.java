package com.huaifang.yan.proxy;

import java.lang.reflect.Proxy;

public class APPMain {

    public static void main(String[] args) {
        // 实例化真实对象
        Sellable realEstate = new RealEstate();

        // 创建代理对象，并将真实对象传给InvocationHandler
        // 这块代码是动态代理的精髓
        Sellable proxy = (Sellable) Proxy.newProxyInstance(
                Sellable.class.getClassLoader(),
                new Class<?>[]{Sellable.class},
                new LoggingInvocationHandler(realEstate)
        );
        System.out.println(proxy.toString());
        // 现在调用的是代理对象的方法，但会触发InvocationHandler的逻辑
        proxy.sell("豪华别墅");
        proxy.buy("大平层");
        System.out.println(proxy.toString());

        // 输出：
        // 开始销售房源操作...
        // 实际销售房源: 豪华别墅
        // 完成销售房源操作.
    }
}
