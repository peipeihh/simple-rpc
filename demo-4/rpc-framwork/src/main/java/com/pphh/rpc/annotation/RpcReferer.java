package com.pphh.rpc.annotation;

import java.lang.annotation.*;

/**
 * Created by huangyinhuang on 1/11/2018.
 * 服务消费方对服务的引用注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReferer {
}
