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

import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Client;
import com.pphh.rpc.transport.Server;
import com.pphh.rpc.transport.http.HttpClient;
import com.pphh.rpc.transport.http.HttpServer;
import com.pphh.rpc.transport.http.ServletEndpoint;
import com.pphh.rpc.util.NetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created by huangyinhuang on 1/11/2018.
 * The data receiving point during a remote service call, which is usually configured on provider side
 * 远程服务调用的通信协议
 */
@Configuration
public class RpcTransportConfig {

    @Value("${rpc.transport.type:http}")
    private String transportType;

    @Value("${rpc.transport.provider.port:9090}")
    private Integer transportPort;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ServletEndpoint(), "/rpc/*");
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.transport.provider.port")
    public Server buildServer() {
        Server server = null;

        InetAddress inetAddress = NetUtil.getLocalAddress();
        URL serverUrl = new URL(inetAddress.getHostAddress(), transportPort);
        switch (transportType) {
            case "http":
                server = new HttpServer(serverUrl);
                break;
            default:
        }

        // start up server
        if (server != null) {
            server.start();
        }

        return server;
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.transport.type")
    public Client buildClient() {
        Client client = null;

        InetAddress inetAddress = NetUtil.getLocalAddress();
        URL serverUrl = new URL(inetAddress.getHostAddress(), transportPort);
        switch (transportType) {
            case "http":
                client = new HttpClient(serverUrl);
                break;
            default:
        }

        return client;
    }

}
