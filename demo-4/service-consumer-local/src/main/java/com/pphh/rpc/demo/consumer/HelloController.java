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
package com.pphh.rpc.demo.consumer;

import com.pphh.rpc.annotation.RpcReferer;
import com.pphh.rpc.demo.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@RestController
public class HelloController {

    @RpcReferer
    private Greeting greetingService;

    @RequestMapping("/hello")
    @ResponseBody
    public String greet() {
        return greetingService.sayHello("Michael");
    }

}
