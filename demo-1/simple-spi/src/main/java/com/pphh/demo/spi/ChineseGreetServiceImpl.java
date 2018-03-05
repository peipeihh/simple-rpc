package com.pphh.demo.spi;

import com.pphh.demo.GreetService;

/**
 * Created by huangyinhuang on 3/1/2018.
 */
public class ChineseGreetServiceImpl implements GreetService {

    @Override
    public String sayHello() {
        return "您好，世界。";
    }

}
