package com.pphh.demo.spi;

import com.pphh.demo.GreetService;

/**
 * Created by huangyinhuang on 3/1/2018.
 */
public class EnglishGreetServiceImpl implements GreetService {

    @Override
    public String sayHello() {
        return "hello,world";
    }

}
