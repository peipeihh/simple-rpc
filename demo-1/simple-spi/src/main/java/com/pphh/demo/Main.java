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
