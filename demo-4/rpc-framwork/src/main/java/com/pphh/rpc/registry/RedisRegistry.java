package com.pphh.rpc.registry;

import com.pphh.rpc.rpc.URL;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mh on 2018/2/19.
 * A redis registry, which store and discover service in a redis cluster
 * To be developed
 */
public class RedisRegistry implements Registry {

    private String redisHostUrl;
    private Set<URL> remoteServiceUrls;

    public RedisRegistry(String redisHostUrl) {
        this.redisHostUrl = redisHostUrl;
        this.remoteServiceUrls = new HashSet<>();
    }

    @Override
    public void register(URL remoteRpcUrl, String serviceName) {
        //TODO: register the provided service to a redis cluster
    }

    @Override
    public void unregister(URL remoteRpcUrl, String serviceName) {
        //TODO: unregister the provided service from a redis cluster
    }

    @Override
    public Set<URL> discover(String serviceName) {
        return this.remoteServiceUrls;
    }

}
