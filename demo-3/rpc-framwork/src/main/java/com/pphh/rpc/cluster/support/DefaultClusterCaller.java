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
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public class DefaultClusterCaller implements ClusterCaller {

    private HaStrategy haStrategy;
    private LoadBalancer loadBalancer;

    public DefaultClusterCaller(HaStrategy haStrategy, LoadBalancer loadBalancer) {
        this.haStrategy = haStrategy;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Response invoke(Request request) {
        return haStrategy.call(request, loadBalancer);
    }

}
