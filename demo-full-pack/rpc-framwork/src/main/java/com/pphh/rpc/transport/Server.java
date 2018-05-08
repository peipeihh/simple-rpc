package com.pphh.rpc.transport;

/**
 * Created by huangyinhuang on 3/13/2018.
 */
public interface Server extends Endpoint {

    /**
     * start the server on the endpoint
     *
     * @return true if server is started successfully
     */
    boolean start();

    /**
     * stop the server on the endpoint
     *
     * @return true if server is reset successfully
     */
    boolean stop();

    /**
     * is bound.
     *
     * @return bound
     */
    boolean isBound();

}
