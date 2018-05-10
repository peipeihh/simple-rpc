package com.pphh.rpc.transport.netty;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 5/10/2018
 */
public class NettyRequestEncoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
        byte[] entity = Serializer.getBytes(request);
        out.writeBytes(entity);

        // 发送结束标记
        out.writeBytes("\r\n".getBytes());
    }

}
