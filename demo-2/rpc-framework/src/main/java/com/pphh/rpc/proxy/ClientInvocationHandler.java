package com.pphh.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public class ClientInvocationHandler implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "Hello, this is client handler.";
    }

}
