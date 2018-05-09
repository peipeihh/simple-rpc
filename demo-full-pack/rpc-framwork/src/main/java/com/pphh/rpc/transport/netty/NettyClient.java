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
package com.pphh.rpc.transport.netty;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Client;

import java.net.InetSocketAddress;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 5/8/2018
 */
public class NettyClient implements Client {

    private URL remoteEndpoint;

    @Override
    public URL getRemoteAddress() {
        return this.remoteEndpoint;
    }

    @Override
    public void setRemoteAddress(URL remoteEndpoint) {
        this.remoteEndpoint = remoteEndpoint;
    }

    @Override
    public URL getLocalAddress() {
        return null;
    }

    private InetSocketAddress getRemoteSocketAddress() {
        return new InetSocketAddress(remoteEndpoint.getHost(), remoteEndpoint.getPort());
    }

    @Override
    public Response invoke(Request request) {
        return null;
    }

}
