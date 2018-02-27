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
import com.pphh.rpc.transport.RemoteService;
import com.pphh.rpc.util.LogUtil;
import sun.rmi.runtime.Log;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailOver is a strategy that will retry the remote call within specified times until it succeeds.
 */
public class FailOverHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {
        Response response = null;

        int tryCount = 3;
        for (int i = 1; i <= tryCount; i++) {

            LogUtil.print("try to visit server..." + i);
            RemoteService remoteService = loadBalancer.select(request);
            if (remoteService != null) {
                response = remoteService.invoke(request);
                Exception remoteException = response.getException();
                if (remoteException != null) {
                    LogUtil.print("Receive an exception on FailOverStrategy, retry with continue...");
                    continue;
                }

                break;
            }

        }

        if (response == null) {
            throw new SimpleRpcException("FailOverStrategy failed to complete remote service call with tries of " + tryCount);
        }

        return response;
    }

}
