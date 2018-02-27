/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
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
