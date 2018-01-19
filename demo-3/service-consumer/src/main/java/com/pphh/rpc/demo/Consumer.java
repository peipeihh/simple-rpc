package com.pphh.rpc.demo;

import com.pphh.rpc.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@SpringBootApplication
public class Consumer {

    public static void main(String[] args) {
        LogUtil.print("app is stared, please go to browser and visit the web service by: http://localhost:8080/hello");
        SpringApplication.run(Consumer.class, args);
    }

}
