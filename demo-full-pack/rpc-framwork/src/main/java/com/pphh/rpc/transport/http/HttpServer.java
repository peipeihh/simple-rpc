package com.pphh.rpc.transport.http;

import com.pphh.rpc.rpc.URL;
import com.pphh.rpc.transport.Server;
import com.pphh.rpc.util.LogUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetSocketAddress;

/**
 * Created by huangyinhuang on 3/13/2018.
 * a http server by jetty
 */
public class HttpServer implements Server {

    private URL serverEndpoint;
    private org.eclipse.jetty.server.Server server;

    public HttpServer(URL url) {
        this.serverEndpoint = url;
        this.server = new org.eclipse.jetty.server.Server(url.getPort());
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return new InetSocketAddress(serverEndpoint.getHost(), serverEndpoint.getPort());
    }

    @Override
    public boolean start() {
        boolean bSuccess = false;

        try {
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            context.addServlet(ServletEndpoint.class, "/" + this.serverEndpoint.getPath());

            HandlerCollection handlers = new HandlerCollection();
            handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
            this.server.setHandler(handlers);
            this.server.start();

            bSuccess = true;
        } catch (Exception e) {
            LogUtil.print("failed to start the jetty server.");
        }

        return bSuccess;
    }

    @Override
    public boolean stop() {
        boolean bSuccess = false;

        try {
            this.server.stop();
            bSuccess = true;
        } catch (Exception e) {
            LogUtil.print("failed to stop the jetty server.");
        }

        return bSuccess;
    }

    @Override
    public boolean isBound() {
//        return this.server.isRunning();
        return false;
    }

}
