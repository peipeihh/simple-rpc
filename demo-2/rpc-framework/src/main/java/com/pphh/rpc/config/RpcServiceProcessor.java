package com.pphh.rpc.config;

import com.pphh.rpc.annotation.RpcService;
import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.transport.ServletEndpoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
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
        ServletEndpoint.PROVIDERS.put(interfaceClass.getName(), provider);
    }

}
