package com.pphh.rpc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@SpringBootApplication
public class Provider {

    public static void main(String[] args) {
        System.out.println("The service is stared and waiting for remote call.");
        SpringApplication.run(Provider.class, args);
    }

}
