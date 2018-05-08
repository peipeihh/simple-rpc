package com.pphh.rpc.transport.http;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pphh.rpc.rpc.*;
import com.pphh.rpc.transport.Client;
import com.pphh.rpc.util.LogUtil;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * Created by huangyinhuang on 3/13/2018.
 * a http client by Unirest
 */
public class HttpClient implements Client, Caller {

    private URL remoteEndpoint;

    public HttpClient(URL remoteEndpoint) {
        this.remoteEndpoint = remoteEndpoint;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return new InetSocketAddress(remoteEndpoint.getHost(), remoteEndpoint.getPort());
    }

    @Override
    public Response invoke(Request request) {
        LogUtil.print("consumer send a remote rpc call to " + this.remoteEndpoint +"...");

        DefaultResponse response = new DefaultResponse();
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

        return response;
    }

}
