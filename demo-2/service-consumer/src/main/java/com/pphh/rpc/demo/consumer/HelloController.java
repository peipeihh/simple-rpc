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

    private Integer visitCount = 0;

    @RequestMapping("/hello")
    @ResponseBody
    public String home() {
        String greetings = greetingService.sayHello("Michael");
        return String.format("%s This service has been visited by %s times.", greetings, visitCount++);
    }

}
