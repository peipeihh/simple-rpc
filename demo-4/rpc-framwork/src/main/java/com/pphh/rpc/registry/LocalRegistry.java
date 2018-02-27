package com.pphh.rpc.registry;

import com.pphh.rpc.rpc.URL;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mh on 2018/2/18.
 * A local in-memory registry, which store itself as remote service URL in local memory
 */
public class LocalRegistry implements Registry {

    private ConcurrentMap<String, Set<URL>> localRegistry;

    public LocalRegistry() {
        localRegistry = new ConcurrentHashMap<>();
    }

    @Override
    public void register(URL url, String serviceName) {
        if (localRegistry.containsKey(serviceName)) {
            Set<URL> urls = localRegistry.get(serviceName);
            if (!urls.contains(url)) {
                urls.add(url);
            }
        } else {
            Set<URL> urls = new HashSet<>();
            urls.add(url);
            localRegistry.put(serviceName, urls);
        }
    }

    @Override
    public void unregister(URL url, String serviceName) {
        if (localRegistry.containsKey(serviceName)) {
            Set<URL> urls = localRegistry.get(serviceName);
            urls.remove(url);
        }
    }

    @Override
    public Set<URL> discover(String serviceName) {
        return this.localRegistry.get(serviceName);
    }

}
