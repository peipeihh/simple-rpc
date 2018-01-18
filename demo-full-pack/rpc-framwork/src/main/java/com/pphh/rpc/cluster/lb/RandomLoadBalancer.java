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
