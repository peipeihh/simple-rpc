
## 说明

本项目对一个简单的远程服务调用进行演示。

一个常用的远程服务调用中，一定会有如下两个必要组件，
- 服务提供方，各个框架一般会以service server/provider命名
- 服务消费方，各个框架一般会以service client/consumer命名

如何让提供方更加容易地暴露服务，并且让消费方更加方便的远程调用，这是远程服务调用框架所要负责的事情。本演示项目中提供一个简单的RPC框架组件，实现了如下功能来支持简单的远程服务调用，
- Java annotation（服务注解）：通过RpcService/RpcReferer注解服务提供方和消费方
- Spring配置：根据annotation配置服务提供方和消费方，在服务提供方初始化服务Provider，在服务消费方初始化ProxyInvocationHandler（调用代理）
- 远程服务调用通信（Http）：配置远程调用的http发送和接受点

在提供方，只需如下定义，就可以实现对远程服务的暴露，
```
@RpcService
public class GreetingImpl implements Greeting {

    public String sayHello() {
        return "Hello,World"
    }

}
```
在消费方，用户只需如下定义，就可以实现对远程服务的引用并使用，
```
public class HelloController {

    @RpcReferer
    private Greeting greetingService;

    public String greet() {
        return greetingService.sayHello();
    }

}
```

更多详细请见下面的演示。

## 项目编译
整个项目目录结构如下，
```
- pom.xml 整个Maven项目的pom文件，这是演示项目的根目录
+ demo-scripts 演示的启动脚本命令
+ rpc-framework 一个简单的RPC框架
  - pom.xml
  + src
    + main/java/com/pphh/rpc/
      + annotation
      + config
      + exception
      + provider
      + proxy
      + rpc
      + transport
      + util
+ service-api 服务接口定义
  - pom.xml
  + src
+ service-consumer 服务消费方
  - pom.xml
  + src
+ service-provider 服务提供方
  - pom.xml
  + src
```
其中rpc-framework子项目是简单RPC框架的实现，service-consumer和service-provider是对这个框架的使用。

请打开shell窗口，切换在演示项目的根目录中，执行如下命令，对项目编译打包，
``` bash
mvn clean package
```

## 演示环境

若无特别声明，演示的shell命令都将在演示项目的根目录中执行。
环境：Windows 7 SP1 + Java 9.0.1 + Maven 3.3.9

## 演示

1. 启动服务提供者在端口8080
   - 打开一个shell窗口，运行如下命令
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-1.0-SNAPSHOT.jar
   java -Dserver.port=8080 -jar %service_provider_jar%
   ```

2. 启动服务消费者在端口8081
   - 打开一个shell窗口，运行如下命令
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-1.0-SNAPSHOT.jar
   java -Dserver.port=8081 -jar %service_consumer_jar%
   ```

3. 访问远程服务调用
   * 打开浏览器，访问如下地址将触发远程服务调用
   ``` bash
   http://localhost:8081/hello
   ```
   * 若一切正常，将可以看到每一次访问调用的日志
   服务消费端日志
   ``` bash
   [20180120 11:10:26-341][http-nio-8081-exec-1] send a remote rpc call from consumer...
   [20180120 11:10:26-543][http-nio-8081-exec-2] send a remote rpc call from consumer...
   [20180120 11:10:26-815][http-nio-8081-exec-3] send a remote rpc call from consumer...
   ```
   服务提供端日志
   ``` bash
   [20180120 11:10:26-867][http-nio-8080-exec-3] receive a remote rpc call in the provider...
   [20180120 11:10:26-867][http-nio-8080-exec-1] receive a remote rpc call in the provider...
   [20180120 11:10:26-867][http-nio-8080-exec-2] receive a remote rpc call in the provider...
   ```
