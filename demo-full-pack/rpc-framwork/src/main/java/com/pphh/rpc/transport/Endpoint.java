package com.pphh.rpc.transport;

import java.net.InetSocketAddress;

/**
 * Created by huangyinhuang on 3/13/2018.
 */
public interface Endpoint {

    /**
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

}
