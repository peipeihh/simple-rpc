package com.pphh.rpc.cluster.lb;

import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by huangyinhuang on 1/17/2018.
 * select the remote service at a random sequence
 */
public class RandomLoadBalancer implements LoadBalancer {

    List<RemoteService> remoteServiceList;


    public RandomLoadBalancer() {
        this.remoteServiceList = new ArrayList<>();
    }

    public RandomLoadBalancer(List<RemoteService> remoteServices) {
        this.remoteServiceList = remoteServices;
    }

    @Override
    public void onRefresh(List<RemoteService> remoteServices) {
        this.remoteServiceList = remoteServices;
    }

    @Override
    public RemoteService select(Request request) {
        RemoteService remoteService = null;

        if (remoteServiceList != null && remoteServiceList.size() > 0) {
            int index = ThreadLocalRandom.current().nextInt(0, remoteServiceList.size());
            remoteService = remoteServiceList.get(index);
        }

        return remoteService;
    }

}
