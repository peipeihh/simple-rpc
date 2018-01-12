package com.pphh.rpc.rpc;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public interface Response {

    /**
     * Store the return object when the request is handled successfully with normal return
     * 若请求正常处理，则返回Object值
     *
     * @return the return object value
     */
    Object getValue();

    /**
     * Store the exception object what's thrown during remote service call
     * 若请求出现异常，则返回Exception信息
     *
     * @return exception object
     */
    Exception getException();

    /**
     * The unique request id
     * 请求ID
     *
     * @return request id
     */
    long getRequestId();

}
