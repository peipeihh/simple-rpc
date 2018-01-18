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
