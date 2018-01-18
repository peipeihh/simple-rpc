package com.pphh.rpc.cluster.ha;

import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.RemoteService;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailOver is a strategy that will try remote call by specified times until it succeeds.
 */
public class FailOverHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {

        int tryCount = 3;
        for (int i = 1; i < tryCount; i++) {
            try {
                RemoteService remoteService = loadBalancer.select(request);
                return remoteService.invoke(request);
            } catch (RuntimeException e) {
                System.out.println("Receive an exception on FailOverStrategy, retry to visit next server..." + i);
            }
        }

        throw new SimpleRpcException("FailOverStrategy failed to complete remote service call with tries of " + tryCount);
    }

}
