/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
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
