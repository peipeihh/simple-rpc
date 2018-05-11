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
