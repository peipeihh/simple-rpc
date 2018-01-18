package com.pphh.rpc.exception;

import org.springframework.beans.BeansException;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public class SimpleRpcException extends BeansException {

    public SimpleRpcException(String msg) {
        super(msg);
    }

    public SimpleRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
