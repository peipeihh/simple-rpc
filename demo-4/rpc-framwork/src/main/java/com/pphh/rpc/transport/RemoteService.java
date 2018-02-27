package com.pphh.rpc.transport;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pphh.rpc.rpc.*;
import com.pphh.rpc.util.LogUtil;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by huangyinhuang on 1/11/2018.
 * The data sending point during a remote service call, which is usually used on consumer side
 * 在远程服务调用中，数据传输层的发送点
 */
public class RemoteService implements Caller {

    private URL remoteUrl;

    public RemoteService(URL url) {
        this.remoteUrl = url;
    }

    public Response invoke(Request request) {
        LogUtil.print("consumer send a remote rpc call to " + this.remoteUrl +"...");

        DefaultResponse response = new DefaultResponse();
        response.setRequestId(request.getRequestId());
        byte[] entity = Serializer.getBytes(request);

        try {
            HttpResponse<String> resp = Unirest.post(this.remoteUrl.toString())
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
