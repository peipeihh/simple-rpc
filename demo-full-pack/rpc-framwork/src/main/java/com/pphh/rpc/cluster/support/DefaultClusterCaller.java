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
package com.pphh.rpc.cluster.support;

import com.pphh.rpc.cluster.ClusterCaller;
import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Client;
import com.pphh.rpc.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public class DefaultClusterCaller implements ClusterCaller {

    private HaStrategy haStrategy;
    private LoadBalancer loadBalancer;
    private Registry registry;
    private Class<? extends Client>  clientClazz;

    public DefaultClusterCaller(HaStrategy haStrategy,
                                LoadBalancer loadBalancer) {
        this.haStrategy = haStrategy;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void setClientType(Class<? extends Client> clientClazz) {
        this.clientClazz = clientClazz;
    }

    @Override
    public Response invoke(Request request) {
        // refresh the remote service providers by registry
        String serviceName = request.getMethodSignature();
        Set<URL> providers = this.registry.discover(serviceName);
        List<Client> remoteServices = getListOfRemoteService(providers);
        this.loadBalancer.onRefresh(remoteServices);

        return haStrategy.call(request, loadBalancer);
    }

    private List<Client> getListOfRemoteService(Set<URL> providers) {
        List<Client> remoteServices = new ArrayList<>();
        if (providers != null) {
            for (URL url : providers) {
                try {
                    Client client = clientClazz.newInstance();
                    client.setRemoteAddress(url);
                    remoteServices.add(client);
                } catch (InstantiationException | IllegalAccessException e) {
                    LogUtil.print("failed to initialize the client instance.");
                }
            }
        }
        return remoteServices;
    }

}
