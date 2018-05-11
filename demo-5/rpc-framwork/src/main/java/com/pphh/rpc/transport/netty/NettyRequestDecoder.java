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
import com.pphh.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * netty request decoder, which helps to read the request object from bytes
 *
 * @author huangyinhuang
 * @date 5/9/2018
 */
public class NettyRequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 查询\r\n在消息中的位置，若消息的结尾不是\r\n，则消息的传输未结束。
        int length = in.readableBytes();
        int keyR = in.indexOf(length, 0, (byte) '\r');
        int keyN = in.indexOf(length, 0, (byte) '\n');

        // 若消息传输未结束，直接返回等待消息传输完毕
        if (keyR == (length - 2) && keyN == (length - 1)) {

            // 反序列化请求
            byte[] data = new byte[length];
            in.readBytes(data, 0, length - 2);

            Request request = Serializer.getRequest(data);
            out.add(request);

        }

    }

}
