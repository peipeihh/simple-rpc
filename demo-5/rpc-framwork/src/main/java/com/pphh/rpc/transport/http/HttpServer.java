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
package com.pphh.rpc.transport.http;

import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Server;
import com.pphh.rpc.util.LogUtil;
import com.pphh.rpc.util.NetUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetAddress;

/**
 * a http server implemented by jetty
 * Created by huangyinhuang on 3/13/2018.
 */
public class HttpServer implements Server {

    private URL serverEndpoint;
    private org.eclipse.jetty.server.Server server;

    public HttpServer(Integer transportPort) {
        InetAddress inetAddress = NetUtil.getLocalAddress();
        this.serverEndpoint = new URL(inetAddress.getHostAddress(), transportPort);
        this.server = new org.eclipse.jetty.server.Server(transportPort);
    }

    @Override
    public URL getLocalAddress() {
        return serverEndpoint;
    }

    @Override
    public boolean start() {
        boolean bSuccess = false;

        try {
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            context.addServlet(ServletEndpoint.class, "/" + this.serverEndpoint.getPath());

            HandlerCollection handlers = new HandlerCollection();
            handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
            this.server.setHandler(handlers);
            this.server.start();

            LogUtil.print("start the jetty server on " + this.serverEndpoint.toString());
            bSuccess = true;
        } catch (Exception e) {
            LogUtil.print("failed to start the jetty server.");
        }

        return bSuccess;
    }

    @Override
    public boolean stop() {
        boolean bSuccess = false;

        try {
            this.server.stop();
            bSuccess = true;
        } catch (Exception e) {
            LogUtil.print("failed to stop the jetty server.");
        }

        return bSuccess;
    }

    @Override
    public boolean isBound() {
        return this.server.isRunning();
    }

}
