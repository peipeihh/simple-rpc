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
import com.pphh.rpc.util.LogUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * a transport client implemented by netty.
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
        Response response = null;

        NettyClientHandler clientHandler = new NettyClientHandler();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        int maxTimeout = 100;

        try {
            Bootstrap boot = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast("Encoder", new NettyRequestEncoder())
                                    .addLast(clientHandler);
                        }
                    });

            // TODO: 为了演示方便，如下使用同步的方式进行netty的连接通信，在实际应用中推荐采用异步方式

            // start the client's connection
            ChannelFuture f = boot.connect(this.remoteEndpoint.getHost(), this.remoteEndpoint.getPort()).sync();

            int timeout = 0;
            do {
                LogUtil.print("channel is not active, wait connection...");
                Thread.sleep(100);
                if (++timeout > maxTimeout) {
                    throw new Exception("failed to connect remote server");
                }
            } while (!f.channel().isActive());
            LogUtil.print("channel is connected with " + this.remoteEndpoint.toString());

            // send the request
            ChannelPromise promise = clientHandler.sendRequest(request);
            promise.await(100);

            // read the response from server
            response = clientHandler.fetchResponse();

            // wait until the connection is closed.
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            LogUtil.print("an exception is thrown when trying to communicate with remote rpc server.");
        } finally {
            workerGroup.shutdownGracefully();
        }

        return response;
    }

}
