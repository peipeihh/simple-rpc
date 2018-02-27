package com.pphh.rpc.registry;

import com.pphh.rpc.rpc.URL;

import java.util.Set;

/**
 * Created by mh on 2018/2/18.
 * A direct registry, which configure remote service URL by manual (usually by environment settings)
 */
public class DirectRegistry implements Registry {

    private Set<URL> remoteServiceUrls;

    public DirectRegistry(Set<URL> urls) {
        this.remoteServiceUrls = urls;
    }

    @Override
    public void register(URL url, String serviceName) {
        // do nothing
    }

    @Override
    public void unregister(URL url, String serviceName) {
        // do nothing
    }

    @Override
    public Set<URL> discover(String serviceName) {
        return this.remoteServiceUrls;
    }

}
