package com.pphh.rpc.cluster;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public interface HaStrategy {

    Response call(Request request, LoadBalancer loadBalance);

}
