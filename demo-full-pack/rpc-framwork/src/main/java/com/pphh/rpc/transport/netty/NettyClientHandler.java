package com.pphh.rpc.transport.netty;

import com.pphh.rpc.rpc.DefaultResponse;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Please add description here.
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
