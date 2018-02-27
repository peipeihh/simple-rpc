package com.pphh.rpc.config;

import com.pphh.rpc.annotation.RpcService;
import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.ServletEndpoint;
import com.pphh.rpc.util.LogUtil;
import com.pphh.rpc.util.NetUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangyinhuang on 1/11/2018.
 * Initialize the service provider bean on service provider side
 * 配置服务提供方，初始化服务Provider
 */
@Configuration
public class RpcServiceProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;


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

        // register the provider and its services
        Registry registry = applicationContext.getBean(Registry.class);
        if (registry != null) {
            String localPort = environment.getProperty("server.port");
            try {
                InetAddress inetAddress = NetUtil.getLocalAddress();
                Integer port = Integer.parseInt(localPort);
                URL url = new URL(inetAddress.getHostAddress(), port);
                Map<String, Method> methodMap = provider.getMethodMap();
                Set<String> serviceList = methodMap.keySet();
                for (String serviceName : serviceList) {
                    registry.register(url, serviceName);
                }
            } catch (NumberFormatException e) {
                LogUtil.print("failed to parse server port.");
            }

        }

    }

}
