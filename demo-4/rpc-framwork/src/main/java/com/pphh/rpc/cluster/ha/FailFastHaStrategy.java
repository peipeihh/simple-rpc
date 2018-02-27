package com.pphh.rpc.cluster.ha;

import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.RemoteService;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailFast is a strategy that make the remote call only once without any retry.
 */
public class FailFastHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {
        Response response = null;

        RemoteService remoteService = loadBalancer.select(request);
        if (remoteService != null) {
            response = remoteService.invoke(request);
        }

        if (response == null) {
            throw new SimpleRpcException("FailFastHaStrategy failed to complete remote service call.");
        }

        return response;
    }

}
