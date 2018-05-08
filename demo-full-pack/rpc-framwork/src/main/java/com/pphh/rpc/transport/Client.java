package com.pphh.rpc.transport;

import com.pphh.rpc.rpc.Caller;

import java.net.InetSocketAddress;

/**
 * Created by huangyinhuang on 3/13/2018.
 */
public interface Client extends Endpoint, Caller {

    /**
     * get remote socket address
     *
     * @return
     */
    InetSocketAddress getRemoteAddress();

}
