package com.pphh.rpc.rpc;

import java.io.Serializable;

/**
 * Created by huangyinhuang on 1/11/2018.
 */

public class DefaultResponse implements Response, Serializable {

    private static final long serialVersionUID = -8924333945227948972L;

    private Object value;
    private Exception exception;
    private long requestId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

}
