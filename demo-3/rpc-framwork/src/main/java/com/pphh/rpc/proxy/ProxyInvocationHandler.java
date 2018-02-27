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
package com.pphh.rpc.proxy;

import com.pphh.rpc.cluster.ClusterCaller;
import com.pphh.rpc.rpc.DefaultRequest;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.RemoteService;
import com.pphh.rpc.util.LogUtil;
import com.pphh.rpc.util.MethodSignUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by huangyinhuang on 1/11/2018.
 * Service Proxy, which will be initialized on service consumer side
 * 客户端服务代理调用
 */
public class ProxyInvocationHandler<T> implements InvocationHandler {

    //    private RemoteService remoteService = new RemoteService();
    public static ClusterCaller clusterCaller;
    private Class<T> interfaceClz;

    public ProxyInvocationHandler(Class<T> interfaceClz) {
        this.interfaceClz = interfaceClz;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method)) {
            if ("toString".equals(method.getName())) {
                return "toString";
            }
            if ("equals".equals(method.getName())) {
                return "equals";
            }
            throw new Exception("can not invoke local method:" + method.getName());
        }

        DefaultRequest request = new DefaultRequest();
        request.setRequestId(UUID.randomUUID().getLeastSignificantBits());
        request.setArguments(args);
        request.setMethodName(method.getName());
        request.setMethodSignature(MethodSignUtil.getMethodSignature(method));
        request.setInterfaceName(this.interfaceClz.getName());
        Class returnType = method.getReturnType();
        request.setReturnType(returnType);

        //Response response = remoteService.invoke(request);
        Response response = clusterCaller.invoke(request);
        LogUtil.print("consumer receive response from remote rpc call, value=" + response.getValue());
        return response.getValue();
    }

    private boolean isLocalMethod(Method method) {
        boolean isLocal = false;

        if (method.getDeclaringClass().equals(Object.class)) {
            try {
                this.interfaceClz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                isLocal = false;
            } catch (Exception e) {
                isLocal = true;
            }
        }

        return isLocal;
    }

}
