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
