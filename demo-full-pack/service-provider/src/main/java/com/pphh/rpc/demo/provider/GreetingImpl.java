package com.pphh.rpc.demo.provider;


import com.pphh.rpc.annotation.RpcService;
import com.pphh.rpc.demo.Greeting;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@RpcService
public class GreetingImpl implements Greeting {

    @Value("${server.port:8080}")
    private String hostPort;

    public String sayHello(String user) {
        return String.format("Hello, %s. This is greetings from localhost:%s at %s. ", user, hostPort, new Date().toString());
    }

}
