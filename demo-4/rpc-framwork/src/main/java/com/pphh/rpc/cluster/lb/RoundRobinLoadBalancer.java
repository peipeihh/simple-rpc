package com.pphh.rpc.cluster.lb;

import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyinhuang on 1/17/2018.
 * select the remote service by turns
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private List<RemoteService> remoteServiceList;
    private int index = 0;

    public RoundRobinLoadBalancer() {
        this.remoteServiceList = new ArrayList<>();
    }

    public RoundRobinLoadBalancer(List<RemoteService> remoteServices) {
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
            if (this.index == remoteServiceList.size()) {
                this.index = 0;
            }
            remoteService = remoteServiceList.get(index++);
        }

        return remoteService;
    }

}
