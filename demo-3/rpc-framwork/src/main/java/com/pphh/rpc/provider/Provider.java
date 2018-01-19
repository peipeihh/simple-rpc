package com.pphh.rpc.provider;

import com.pphh.rpc.rpc.Caller;
import com.pphh.rpc.rpc.DefaultResponse;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.util.MethodSignUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangyinhuang on 1/11/2018.
 * Service Provider, which will be initialized on remote service provider side
 * 服务端代理调用
 */
public class Provider<T> implements Caller {

    private T serviceInstance;
    private Class<T> clz;
    private Map<String, Method> methodMap;

    public Provider(T serviceInstance, Class<T> clz) {
        this.serviceInstance = serviceInstance;
        this.clz = clz;
        initMethodMap(clz);
    }

    public Response invoke(Request request) {
        DefaultResponse response = new DefaultResponse();
        Method method = lookupMethod(request.getMethodName(), request.getMethodSignature());

        try {
            Object value = method.invoke(this.serviceInstance, request.getArguments());
            response.setValue(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.setException(e);
        }

        response.setRequestId(request.getRequestId());
        return response;
    }

    public Method lookupMethod(String methodName, String methodSign) {
        Method method = null;
        if (this.methodMap.containsKey(methodName)) {
            method = this.methodMap.get(methodName);
        } else {
            method = this.methodMap.get(methodSign);
        }
        return method;
    }

    private void initMethodMap(Class<T> clazz) {
        this.methodMap = new HashMap<>();
        Method[] methods = clazz.getMethods();
        Map<String, List<Method>> nameMethodMap = new HashMap<>();
        for (Method method : methods) {
            List<Method> nameMethods = nameMethodMap.get(method.getName());
            if (nameMethods == null){
                nameMethods = new ArrayList<>();
                nameMethodMap.put(method.getName(), nameMethods);
            }
            nameMethods.add(method);
        }
        for (String methodName : nameMethodMap.keySet()) {
            List<Method> nameMethods = nameMethodMap.get(methodName);
            if (nameMethods.size() == 1) {
                Method method = nameMethods.get(0);
                this.methodMap.put(methodName, method);
            }
            for (Method method : nameMethods) {
                this.methodMap.put(MethodSignUtil.getMethodSignature(method), method);
            }
        }
    }

}
