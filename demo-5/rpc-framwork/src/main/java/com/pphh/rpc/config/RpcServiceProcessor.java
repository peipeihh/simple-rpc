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

import com.pphh.rpc.annotation.RpcService;
import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.provider.RpcProviderResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by huangyinhuang on 1/11/2018.
 * Initialize the service provider bean on service provider side
 * 配置服务提供方，初始化服务Provider
 */
@Configuration
public class RpcServiceProcessor implements BeanPostProcessor {

    @Autowired
    Environment environment;
    @Autowired
    private ApplicationContext applicationContext;
//    @Autowired
//    private RegistryScheduler registryScheduler;

    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        Annotation annotation = AnnotationUtils.findAnnotation(bean.getClass(), RpcService.class);
        if (annotation == null) {
            return bean;
        }

        // register the service by each interfaces
        Set<Class<?>> interfaceClasses = ClassUtils.getAllInterfacesAsSet(bean);
        for (Class<?> interfaceClass : interfaceClasses) {
            registerRpcService(interfaceClass, bean);
        }
        return bean;
    }

    private void registerRpcService(Class interfaceClass, Object bean) {
        Provider<?> provider = new Provider<>(bean, interfaceClass);
        RpcProviderResource.PROVIDERS.put(interfaceClass.getName(), provider);
    }

}
