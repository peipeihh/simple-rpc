package com.pphh.rpc.config;

import com.pphh.rpc.exception.SimpleBeansException;
import com.pphh.rpc.annotation.RpcReferer;
import com.pphh.rpc.proxy.ClientInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by huangyinhuang on 1/11/2018.
 * 配置服务消费方，实例化的服务引用Bean
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
                throw new SimpleBeansException("Failed to init rpc client reference at filed " + field.getName()
                        + " in class " + bean.getClass().getName(), e);
            }
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    private Object proxy(Class<?> referenceClass) {
        ClientInvocationHandler clientHandler = new ClientInvocationHandler();
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{referenceClass}, clientHandler);
    }

}
