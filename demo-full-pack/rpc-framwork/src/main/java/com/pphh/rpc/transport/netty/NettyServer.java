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

import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Server;
import com.pphh.rpc.util.NetUtil;

import java.net.InetAddress;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 5/8/2018
 */
public class NettyServer implements Server {

    private URL serverEndpoint;

    public NettyServer(Integer transportPort) {
        InetAddress inetAddress = NetUtil.getLocalAddress();
        this.serverEndpoint = new URL(inetAddress.getHostAddress(), transportPort);
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public URL getLocalAddress() {
        return null;
    }

}
