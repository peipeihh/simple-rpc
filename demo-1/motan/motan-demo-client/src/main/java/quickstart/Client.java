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
package quickstart;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huangyinhuang on 1/19/2018.
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:motan_client.xml");

        FooService service = (FooService) ctx.getBean("remoteService");
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                System.out.println(service.hello("motan" + i));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("a exception is received on motan" + i);
            }
            Thread.sleep(2000);
        }

        System.out.println("motan demo is finish.");
        System.exit(0);
    }
}
