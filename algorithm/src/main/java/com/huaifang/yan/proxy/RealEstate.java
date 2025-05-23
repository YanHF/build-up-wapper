package com.huaifang.yan.proxy;

// 接口的实现类：房地产公司
public class RealEstate implements Sellable {
    @Override
    public void sell(String item) {
        System.out.println("实际销售房源: " + item);
    }

    @Override
    public void buy(String item) {
        System.out.println("实际购买房源: " + item);
    }
}
