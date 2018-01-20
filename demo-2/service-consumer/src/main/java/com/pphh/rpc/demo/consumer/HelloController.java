package com.pphh.rpc.demo.consumer;

import com.pphh.rpc.annotation.RpcReferer;
import com.pphh.rpc.demo.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huangyinhuang on 1/11/2018.
 */
@RestController
public class HelloController {

    @RpcReferer
    private Greeting greetingService;

    @RequestMapping("/hello")
    @ResponseBody
    public String greet() {
        return greetingService.sayHello("Michael");
    }

}
