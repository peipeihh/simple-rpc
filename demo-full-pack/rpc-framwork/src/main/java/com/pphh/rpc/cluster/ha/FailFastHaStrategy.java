package com.pphh.rpc.cluster.ha;

import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.RemoteService;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailFast is a strategy that send the remote call only once and reports failure once it failed.
 */
public class FailFastHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {
        RemoteService remoteService = loadBalancer.select(request);
        return remoteService.invoke(request);
    }

}
