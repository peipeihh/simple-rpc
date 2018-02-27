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
package com.pphh.rpc.config;

import com.pphh.rpc.annotation.RpcReferer;
import com.pphh.rpc.cluster.ClusterCaller;
import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.proxy.ProxyInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by huangyinhuang on 1/11/2018.
 * Initialize the service bean reference on service consumer side
 * 配置服务消费方，实例化服务引用Bean
 */
@Configuration
public class RpcRefererProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                RpcReferer reference = field.getAnnotation(RpcReferer.class);
                if (reference != null) {
                    Object value = proxy(field.getType());
                    if (value != null) {
                        field.set(bean, value);
                    }
                }
            } catch (Exception e) {
                throw new SimpleRpcException("Failed to init rpc client reference at filed " + field.getName()
                        + " in class " + bean.getClass().getName(), e);
            }
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    private Object proxy(Class<?> referenceClass) {
        //ClusterCaller cc = (ClusterCaller) applicationContext.getBean("ClusterCaller");
        ProxyInvocationHandler proxyHandler = new ProxyInvocationHandler(referenceClass);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{referenceClass}, proxyHandler);
    }

}
