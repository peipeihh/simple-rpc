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
package com.pphh.demo;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by huangyinhuang on 3/5/2018.
 */
public class Main {

    public static void main(String[] args){
        ServiceLoader<GreetService> serviceLoader = ServiceLoader.load(GreetService.class);
        Iterator<GreetService> iterator = serviceLoader.iterator();
        while (iterator!=null && iterator.hasNext()){
            GreetService greetService = iterator.next();
            System.out.println("Class Name:" + greetService.getClass().getName());
            System.out.println( greetService.sayHello());
        }
    }

}
