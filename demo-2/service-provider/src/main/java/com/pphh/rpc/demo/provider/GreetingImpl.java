package com.pphh.rpc.demo.provider;


import com.pphh.rpc.annotation.RpcService;
import com.pphh.rpc.demo.Greeting;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@RpcService
public class GreetingImpl implements Greeting {

    private Integer visitCount = 0;

    public String sayHello(String user) {
        return String.format("Hello, %s. This service has been visited by %s times.", user, visitCount++);
    }

}
