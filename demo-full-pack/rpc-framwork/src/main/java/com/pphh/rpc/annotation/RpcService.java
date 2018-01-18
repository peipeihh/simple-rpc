package com.pphh.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * Created by huangyinhuang on 1/11/2018.
 * 服务提供方对服务的注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface RpcService {
}
