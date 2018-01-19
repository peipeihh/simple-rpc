package com.pphh.rpc.cluster;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.transport.RemoteService;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public interface LoadBalancer {

    RemoteService select(Request request);

}
