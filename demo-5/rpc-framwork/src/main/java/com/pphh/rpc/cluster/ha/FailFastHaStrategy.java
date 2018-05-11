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
package com.pphh.rpc.cluster.ha;

import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.Client;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailFast is a strategy that make the remote call only once without any retry.
 */
public class FailFastHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {
        Response response = null;

        Client remoteService = loadBalancer.select(request);
        if (remoteService != null) {
            response = remoteService.invoke(request);
        }

        if (response == null) {
            throw new SimpleRpcException("FailFastHaStrategy failed to complete remote service call.");
        }

        return response;
    }

}
