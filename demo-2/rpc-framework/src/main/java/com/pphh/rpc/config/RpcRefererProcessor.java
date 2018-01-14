package com.pphh.rpc.config;

import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.annotation.RpcReferer;
import com.pphh.rpc.proxy.ProxyInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
        ProxyInvocationHandler proxyHandler = new ProxyInvocationHandler(referenceClass);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{referenceClass}, proxyHandler);
    }

}
