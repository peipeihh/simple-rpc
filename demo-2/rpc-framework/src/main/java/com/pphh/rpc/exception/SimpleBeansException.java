package com.pphh.rpc.exception;

import org.springframework.beans.BeansException;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public class SimpleBeansException extends BeansException {

    public SimpleBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
