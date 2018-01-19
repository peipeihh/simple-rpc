package com.alibaba.dubbo.demo.provider;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"provider.xml"});
        context.start();
        System.in.read(); // 按任意键退出
    }
}
