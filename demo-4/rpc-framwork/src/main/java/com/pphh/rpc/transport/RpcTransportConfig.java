package com.pphh.rpc.transport;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huangyinhuang on 1/11/2018.
 * The data receiving point during a remote service call, which is usually configured on provider side
 * 远程服务调用的数据传输接受点，一般配置在服务提供端中
 */
@Configuration
public class RpcTransportConfig {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ServletEndpoint(), "/rpc/*");
    }

}
