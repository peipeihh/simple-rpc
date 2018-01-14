package com.pphh.rpc.transport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pphh.rpc.provider.Provider;
import com.pphh.rpc.rpc.Request;
import com.pphh.rpc.rpc.Response;
import com.pphh.rpc.util.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by huangyinhuang on 1/11/2018.
 * The data receiving point during a remote service call, which is usually used on provider side
 * 在远程服务调用中，数据传输层的接受点
 */
public class ServletEndpoint extends HttpServlet {

    public static Map<String, Provider<?>> PROVIDERS = new ConcurrentHashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogUtil.print("receive a remote rpc call in the provider...");

        InputStream input = req.getInputStream();
        byte[] data = IOUtils.toByteArray(input);
        input.close();
        Request request = Serializer.getRequest(data);

        // invoke the service call by request
        Provider provider = PROVIDERS.get(request.getInterfaceName());
        if (provider != null) {
            Response response = provider.invoke(request);

            // return the result
            OutputStream out = resp.getOutputStream();
            byte[] resultBytes = null;
            if (response.getValue() != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resultBytes = SerializationUtils.serialize((Serializable) response.getValue());
                //Class clz = request.getReturnType();
                //resultBytes = convertToBytes(clz.cast(response.getValue()).toString());
            } else if (response.getException() != null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resultBytes = "The remote service call run into an exception.".getBytes();
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resultBytes = "The remote service call doesn't return any results.".getBytes();
            }
            out.write(resultBytes);
            out.flush();
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            OutputStream out = resp.getOutputStream();
            String msg = String.format("A bad reqeust was received from consumer.");
            out.write(msg.getBytes());
            out.flush();
        }

    }

    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

}
