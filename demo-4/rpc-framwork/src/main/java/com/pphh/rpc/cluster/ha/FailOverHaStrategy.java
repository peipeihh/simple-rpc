package com.pphh.rpc.cluster.ha;

import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.exception.SimpleRpcException;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.transport.RemoteService;
import com.pphh.rpc.util.LogUtil;
import sun.rmi.runtime.Log;

/**
 * Created by huangyinhuang on 1/17/2018.
 * FailOver is a strategy that will retry the remote call within specified times until it succeeds.
 */
public class FailOverHaStrategy implements HaStrategy {

    @Override
    public Response call(Request request, LoadBalancer loadBalancer) {
        Response response = null;

        int tryCount = 3;
        for (int i = 1; i <= tryCount; i++) {

            LogUtil.print("try to visit server..." + i);
            RemoteService remoteService = loadBalancer.select(request);
            if (remoteService != null) {
                response = remoteService.invoke(request);
                Exception remoteException = response.getException();
                if (remoteException != null) {
                    LogUtil.print("Receive an exception on FailOverStrategy, retry with continue...");
                    continue;
                }

                break;
            }

        }

        if (response == null) {
            throw new SimpleRpcException("FailOverStrategy failed to complete remote service call with tries of " + tryCount);
        }

        return response;
    }

}
