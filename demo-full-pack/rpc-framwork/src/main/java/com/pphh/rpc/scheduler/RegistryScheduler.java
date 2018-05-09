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
package com.pphh.rpc.scheduler;

import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.registry.LocalRegistry;
import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Server;
import com.pphh.rpc.transport.http.ServletEndpoint;
import com.pphh.rpc.util.LogUtil;
import com.pphh.rpc.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangyinhuang on 3/5/2018.
 */
@EnableScheduling
@ConditionalOnProperty(name = "rpc.service.heartbeat.report.enable", havingValue = "true")
@Configuration
public class RegistryScheduler {

    @Autowired
    Environment environment;
    @Autowired
    private ApplicationContext applicationContext;

    @Scheduled(initialDelayString = "${rpc.service.heartbeat.report.initialDelay}",
            fixedRateString = "${rpc.service.heartbeat.report.fixedRate}")
    public void refreshServiceStatus() {
        LogUtil.print("refresh service status by registering the provider again");

        Registry registry = applicationContext.getBean(Registry.class);
        if (registry != null) {

            try {
                Server server = applicationContext.getBean(Server.class);
                //URL url = new URL(server.getLocalAddress().getAddress().getHostAddress(), server.getLocalAddress().getPort());
                URL url = server.getLocalAddress();

                for (Map.Entry<String, Provider<?>> entrySet : ServletEndpoint.PROVIDERS.entrySet()) {
                    Provider provider = entrySet.getValue();
                    Map<String, Method> methodMap = provider.getMethodMap();
                    Set<String> serviceList = methodMap.keySet();
                    for (String serviceName : serviceList) {
                        registry.register(url, serviceName);
                    }
                }

            } catch (NumberFormatException e) {
                LogUtil.print("failed to parse server port.");
            }

        }
    }
}
