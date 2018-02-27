package com.pphh.rpc.registry;


import com.pphh.rpc.rpc.URL;

import java.util.Set;

/**
 * Created by mh on 2018/2/18.
 */
public interface Registry {

    /**
     * register the service name what's provided by the rpc server
     *
     * @param remoteRpcUrl the url of remote rpc provider
     * @param serviceName  the provided service name to be register
     */
    void register(URL remoteRpcUrl, String serviceName);

    /**
     * unregister the service name from the rpc server
     *
     * @param remoteRpcUrl the url of remote rpc provider
     * @param serviceName  the service name to be unregister
     */
    void unregister(URL remoteRpcUrl, String serviceName);

    /**
     * look up the list of rpc providers' URL which could provide the service
     *
     * @param serviceName the service name
     * @return the list of available rpc providers' URL
     */
    Set<URL> discover(String serviceName);


}
