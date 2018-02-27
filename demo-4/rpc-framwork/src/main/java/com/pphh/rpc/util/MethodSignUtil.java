package com.pphh.rpc.util;

import java.lang.reflect.Method;

/**
 * Created by huangyinhuang on 1/12/2018.
 */
public class MethodSignUtil {

    public static String getMethodSignature(Method method) {
        Class clz = method.getDeclaringClass();
        return getMethodSignature(clz.getName(), method.getName(), method.getParameterTypes());
    }

    private static String getMethodSignature(String clzName, String methodName, Class<?>[] parameterTypes) {
        return getMethodSignature(clzName, methodName, getMethodParameterTypes(parameterTypes));
    }

    public static String[] getMethodParameterTypes(Class<?>[] parameterTypes) {
        String[] parameterTypeStrings = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypeStrings[i] = parameterTypes[i].getName();
        }
        return parameterTypeStrings;
    }

    public static String getMethodSignature(String clzName, String methodName, String[] parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(clzName);
        builder.append(".");
        builder.append(methodName);
        if (parameterTypes != null) {
            builder.append("(");
            builder.append(StringUtils.join('|', parameterTypes));
            builder.append(")");
        }
        return builder.toString();
    }

}
