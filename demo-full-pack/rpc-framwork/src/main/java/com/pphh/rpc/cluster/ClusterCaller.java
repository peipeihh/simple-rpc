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
package com.pphh.rpc.cluster;

import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.rpc.Caller;
import com.pphh.rpc.transport.Client;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public interface ClusterCaller extends Caller {

    /**
     * refresh registry, which helps to discover remote service cluster
     *
     * @param registry a service registry
     */
    void setRegistry(Registry registry);

    /**
     * set client class, which will be used to initialize all client instances
     *
     * @param clientClazz client class
     */
    void setClientType(Class<? extends Client> clientClazz);

}
