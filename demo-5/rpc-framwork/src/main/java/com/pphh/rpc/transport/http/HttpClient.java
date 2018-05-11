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
package com.pphh.rpc.transport.http;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pphh.rpc.rpc.*;
import com.pphh.rpc.serializer.Serializer;
import com.pphh.rpc.transport.Client;
import com.pphh.rpc.util.LogUtil;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * a http client implemented by Unirest
 * Created by huangyinhuang on 3/13/2018.
 */
public class HttpClient implements Client, Caller {

    private URL remoteEndpoint;

    @Override
    public URL getRemoteAddress() {
        return remoteEndpoint;
    }

    @Override
    public void setRemoteAddress(URL remoteEndpoint) {
        this.remoteEndpoint = remoteEndpoint;
    }

    @Override
    public URL getLocalAddress() {
        return null;
    }

    @Override
    public Response invoke(Request request) {
        DefaultResponse response = null;

        if (this.remoteEndpoint != null) {
            LogUtil.print("consumer send a remote rpc call to " + this.remoteEndpoint + "...");

            response = new DefaultResponse();
            response.setRequestId(request.getRequestId());
            byte[] entity = Serializer.getBytes(request);

            try {
                HttpResponse<String> resp = Unirest.post(this.remoteEndpoint.toString())
                        .header("cache-control", "no-cache")
                        .body(entity)
                        .asString();

                InputStream input = resp.getRawBody();
                Object result = SerializationUtils.deserialize(input);
                input.close();

                Class clz = request.getReturnType();
                response.setValue(clz.cast(result));
            } catch (UnirestException | IOException e) {
                //e.printStackTrace();
                response.setException(e);
            }
        }

        return response;
    }
}
