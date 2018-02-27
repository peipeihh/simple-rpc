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
