package com.pphh.rpc.rpc;

import java.io.Serializable;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
public class DefaultRequest implements Request, Serializable {

    private static final long serialVersionUID = 6034202747524206662L;
    private String interfaceName;
    private String methodName;
    private String methodSignature;
    private Object[] arguments;
    private long requestId;
    private Class<?> returnType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

}
