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
import com.pphh.rpc.util.LogUtil;
import com.pphh.rpc.util.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 5/8/2018
 */
public class NettyServer implements Server {

    private URL serverEndpoint;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public NettyServer(Integer transportPort) {
        InetAddress inetAddress = NetUtil.getLocalAddress();
        this.serverEndpoint = new URL("netty", inetAddress.getHostAddress(), transportPort, "", null);
    }

    @Override
    public boolean start() {
        boolean bSuccess = false;

        try {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap boot = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast("Decoder", new NettyRequestDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });

            // start the server
            ChannelFuture channelFuture = boot.bind(this.serverEndpoint.getPort()).syncUninterruptibly();
            serverChannel = channelFuture.channel();

            LogUtil.print("start the netty server on " + this.serverEndpoint.toString());
            bSuccess = true;
        } catch (Exception e) {
            LogUtil.print("failed to start the netty server.");
            this.stop();
        }

        return bSuccess;
    }

    @Override
    public boolean stop() {
        if (serverChannel != null) {
            serverChannel.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        return true;
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public URL getLocalAddress() {
        return serverEndpoint;
    }

}
