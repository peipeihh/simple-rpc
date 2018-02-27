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
package com.pphh.rpc.cluster.lb;

import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by huangyinhuang on 1/17/2018.
 * select the remote service at a random sequence
 */
public class RandomLoadBalancer implements LoadBalancer {

    List<RemoteService> remoteServiceList;

    public RandomLoadBalancer(List<RemoteService> remoteServices) {
        this.remoteServiceList = remoteServices;
    }

    @Override
    public RemoteService select(Request request) {
        int index = ThreadLocalRandom.current().nextInt(0, remoteServiceList.size());
        return remoteServiceList.get(index);
    }

}
