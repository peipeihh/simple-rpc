package com.pphh.rpc.cluster.lb;

import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

import java.util.List;

/**
 * Created by huangyinhuang on 1/17/2018.
 * select the remote service by turns
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private List<RemoteService> remoteServiceList;
    private int index = 0;

    public RoundRobinLoadBalancer(List<RemoteService> remoteServices) {
        this.remoteServiceList = remoteServices;
    }

    @Override
    public RemoteService select(Request request) {
        if (this.index == remoteServiceList.size()) {
            this.index = 0;
        }
        return remoteServiceList.get(index++);
    }

}
