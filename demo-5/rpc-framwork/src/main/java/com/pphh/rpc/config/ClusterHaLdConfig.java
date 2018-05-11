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

import com.pphh.rpc.cluster.ClusterCaller;
import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.cluster.ha.FailFastHaStrategy;
import com.pphh.rpc.cluster.ha.FailOverHaStrategy;
import com.pphh.rpc.cluster.lb.RandomLoadBalancer;
import com.pphh.rpc.cluster.lb.RoundRobinLoadBalancer;
import com.pphh.rpc.cluster.support.DefaultClusterCaller;
import com.pphh.rpc.proxy.ProxyInvocationHandler;
import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.transport.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by huangyinhuang on 1/18/2018.
 * Initialize the cluster's HA (high availability) and load balance module on service client side
 */
@Configuration
public class ClusterHaLdConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.lb", matchIfMissing = true)
    public LoadBalancer buildLoadBalance(@Value("${rpc.client.lb:random}") String lbType) {
        LoadBalancer lb = null;

        if (lbType.equals("random")) {
            lb = new RandomLoadBalancer();
        } else if (lbType.equals("roundrobin")) {
            lb = new RoundRobinLoadBalancer();
        }

        return lb;
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.ha", matchIfMissing = true)
    public HaStrategy buildFailOverHaStrategy(@Value("${rpc.client.ha:failfast}") String haType) {
        HaStrategy ha = null;

        if (haType.equals("failfast")) {
            ha = new FailFastHaStrategy();
        } else if (haType.equals("failover")) {
            ha = new FailOverHaStrategy();
        }

        return ha;
    }

    @Bean
    @ConditionalOnBean({HaStrategy.class, LoadBalancer.class, Registry.class})
    public ClusterCaller buildClusterCaller(HaStrategy ha,
                                            LoadBalancer lb,
                                            Registry registry,
                                            Client client) {
        ClusterCaller cc = new DefaultClusterCaller(ha, lb);
        cc.setRegistry(registry);
        cc.setClientType(client.getClass());
        ProxyInvocationHandler.clusterCaller = cc;
        return cc;
    }

}
