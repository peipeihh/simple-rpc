package com.pphh.rpc.cluster;

import com.pphh.rpc.registry.Registry;
import com.pphh.rpc.rpc.Caller;

/**
 * Created by huangyinhuang on 1/17/2018.
 */
public interface ClusterCaller extends Caller {

    /**
     * refresh remote service cluster by updated registry
     *
     * @param registry a service registry
     */
    void refreshByRegistry(Registry registry);

}
