package com.pphh.rpc.cluster;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

import java.util.List;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public interface LoadBalancer {

    void onRefresh(List<RemoteService> remoteServices);

    RemoteService select(Request request);

}
