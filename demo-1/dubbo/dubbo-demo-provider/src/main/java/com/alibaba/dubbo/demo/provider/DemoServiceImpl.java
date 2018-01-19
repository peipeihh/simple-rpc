package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.DemoService;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
