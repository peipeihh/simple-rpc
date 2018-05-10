package com.pphh.rpc.transport.netty;

import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Please add description here.
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
