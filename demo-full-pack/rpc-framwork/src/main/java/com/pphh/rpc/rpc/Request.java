package com.pphh.rpc.rpc;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public interface Request {

    /**
     * Remote Service's interface
     * 请求调用的服务接口名
     *
     * @return interface name
     */
    String getInterfaceName();

    /**
     * Remote Service's interface method
     * 请求调用的服务方法名
     *
     * @return method name
     */
    String getMethodName();

    /**
     * Remote Service's interface method signature
     * 请求调用的方法签名，用于查询定位方法
     *
     * @return method signature
     */
    String getMethodSignature();

    /**
     * The method arguments
     * 请求调用的服务方法参数
     *
     * @return method arguments
     */
    Object[] getArguments();


    /**
     * A unique ID
     * 请求ID
     *
     * @return request Id
     */
    long getRequestId();

    /**
     * The class type of return object by remote service call
     * 请求调用的返回类型
     *
     * @return the class type of return object
     */
    Class<?> getReturnType();


}
