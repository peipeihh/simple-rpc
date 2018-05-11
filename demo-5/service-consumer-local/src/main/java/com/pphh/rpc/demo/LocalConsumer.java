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
package com.pphh.rpc.demo;

import com.pphh.rpc.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@SpringBootApplication
public class LocalConsumer {

    public static void main(String[] args) {
        LogUtil.print("app is stared, please go to browser and visit the web service by: http://localhost:8080/hello");
        SpringApplication.run(LocalConsumer.class, args);
    }

}
