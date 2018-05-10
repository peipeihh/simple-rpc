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

import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.provider.RpcProviderResource;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * netty server handler
 *
 * @author huangyinhuang
 * @date 5/9/2018
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LogUtil.print("channelRead");
        Object respMsg;
        if (msg != null && msg instanceof Request) {
            // 获取请求信息
            Request request = (Request) msg;

            Provider provider = RpcProviderResource.PROVIDERS.get(request.getInterfaceName());
            if (provider != null) {

                // 执行服务调用
                Response response = provider.invoke(request);

                if (response.getValue() != null) {
                    respMsg = response.getValue();
                } else if (response.getException() != null) {
                    respMsg = "The remote service call run into an exception.";
                } else {
                    respMsg = "The remote service call doesn't return any results.";
                }

            } else {
                respMsg = String.format("No provider is found for the request: %s", request.getInterfaceName());
            }
        } else {
            respMsg = String.format("NettyServerHandler received unexpected message object.");
        }
        LogUtil.print(respMsg.toString());

        // 返回消息
        byte[] respBytes = SerializationUtils.serialize((Serializable) respMsg);
        ByteBuf buffer = ctx.alloc().buffer(respBytes.length);
        buffer.writeBytes(respBytes);
        ctx.write(buffer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LogUtil.print("channelReadComplete");
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
