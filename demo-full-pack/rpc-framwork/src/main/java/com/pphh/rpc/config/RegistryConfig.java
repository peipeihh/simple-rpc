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

import com.pphh.rpc.registry.*;
import com.pphh.rpc.rpc.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mh on 2018/2/18.
 * Initialize the service registry
 */
@Configuration
public class RegistryConfig {

    @Value("${rpc.registry.type:direct}")
    private String registryType;

    @Value("${rpc.registry.host:}")
    private String registryHost;

    @Value("${rpc.registry.direct.remote:http://localhost:9090/rpc}")
    private String directRemoteHosts;

    @Bean
    public Registry buildRegistry() {
        Registry registry = null;

        switch (registryType) {
            case "direct":
                Set<URL> providers = new HashSet<>();
                String[] urls = directRemoteHosts.split(",");
                for (String url : urls) {
                    URL provider = URL.valueOf(url);
                    if (provider != null) {
                        providers.add(provider);
                    }
                }
                registry = new DirectRegistry(providers);
                break;
            case "local":
                registry = new LocalRegistry();
                break;
            case "redis":
                registry = new RedisRegistry(registryHost);
                break;
            case "file":
                registry = new FileRegistry(registryHost);
                break;
            default:
        }

        return registry;
    }

}
