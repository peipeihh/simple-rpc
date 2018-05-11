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

import com.pphh.rpc.rpc.DefaultResponse;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Netty client handler
 *
 * @author huangyinhuang
 * @date 5/10/2018
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    private ChannelPromise promise;
    private Request request;
    private DefaultResponse response;
    private ByteBuf buffer;

    public ChannelPromise sendRequest(Request request) throws Exception {
        if (this.ctx == null) {
            throw new Exception("ChannelHandlerContext is null, please initalized the context before sending request");
        }

        this.buffer = ctx.alloc().buffer(2048);
        this.request = request;
        return ctx.writeAndFlush(request).channel().newPromise();
    }

    public Response fetchResponse() {
        this.response = new DefaultResponse();
        this.response.setRequestId(request.getRequestId());

        if (this.buffer != null) {
            int dataLength = buffer.readableBytes();
            byte[] bytes = new byte[dataLength];
            buffer.readBytes(bytes);

            Object result = SerializationUtils.deserialize(bytes);
            Class clz = this.request.getReturnType();
            response.setValue(clz.cast(result));
        }

        return this.response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUtil.print("channelActive");
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LogUtil.print("channelRead");

        ByteBuf data = (ByteBuf) msg;
        while (data.isReadable()) {
            int length = this.buffer.writableBytes();
            LogUtil.print("writable bytes: " + length);

            this.buffer.writeBytes(data);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LogUtil.print("channelReadComplete");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
