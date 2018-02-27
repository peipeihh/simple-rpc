package com.pphh.rpc.transport;

import com.pphh.rpc.rpc.Request;

import java.io.*;

/**
 * Created by huangyinhuang on 1/12/2018.
 * The serialization and deserialization during data transport on a remote service call
 * 数据在传输前后的序列化和反序列化
 */
public class Serializer {

    public static byte[] getBytes(Request request) {
        byte[] bytes = null;

        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOutStream);
            out.writeObject(request);
            out.flush();
            bytes = byteOutStream.toByteArray();
            out.close();
            byteOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static Request getRequest(byte[] bytes) {
        Request request = null;

        try {
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(byteInStream);
            request = (Request) in.readObject();
            in.close();
            byteInStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return request;
    }
}
