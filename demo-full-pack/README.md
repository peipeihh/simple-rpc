
## 说明

本项目演示远程服务调用的通信协议（transport）组件。

远程服务调用中，通信组件是一个底层实现，这个组件的性能优劣将直接影响到远程服务调用的吞吐量和稳定性。

通信组件一般包括服务器和客户端两大功能组件，
- 通信服务器：运行在RPC服务提供者方
- 通信客户端：用于RPC消费者方，向远程服务提供者发起服务调用

在项目中，分别抽象为如下接口类，
- com.pphh.rpc.transport.Server
- com.pphh.rpc.transport.Client

通信组件的实现比较多样化，常见的有如下几种，
- Netty
- Apache Mina
- Grizzly
- Jetty
Netty/Apache Mina/Grizzly可以提供包括HTTP/TCP的通信协议，而Jetty主要提供HTTP的通信协议。

在本演示项目中，主要实现了基于Jetty来提供的HTTP通信协议。在启动演示程序时，可以通过下面两个环境变量来进行配置，
- rpc.transport.type = http
- rpc.transport.provider.port = 9090
上面的配置含义为：使用基于Jetty来提供的HTTP通信协议，并将服务器运行在9090端口

更多详细请见下面的演示。

## 项目编译
整个项目目录结构如下，
```
- pom.xml 整个Maven项目的pom文件，这是演示项目的根目录
+ demo-scripts 演示的启动脚本命令
+ rpc-framework
  - pom.xml
  + src
    + main/java/com/pphh/rpc
      + config
        - RpcTransportConfig 通信协议的配置
      + scheduler
        - RegistryScheduler 通信服务接口的定时更新
      + transport
        - Client 通信客户端
        - Server 通信服务器
        + http 基于Jetty提供的HTTP通信协议实现
+ service-api 服务接口声明
  - pom.xml
  + src
+ service-consumer-local 演示注册中心的本地连接
  - pom.xml
  + src
+ service-consumer 演示注册中心的按配置进行远程连接
  - pom.xml
  + src
+ service-provider 服务提供者
  - pom.xml
  + src
```
其中通信协议的实现在rpc-framework子项目中的com.pphh.rpc.transport中。

请打开shell窗口，切换在演示项目的根目录中，执行如下命令，对项目编译打包，
``` bash
mvn clean package
```

## 演示环境

若无特别声明，演示的shell命令都将在演示项目的根目录中执行。
环境：Windows 7 SP1 + Java 9.0.1 + Maven 3.3.9

## 演示

1. 下面的演示中将分别启动服务消费者和提供者在如下端口，
   * 运行服务消费者在9000端口
   * 运行服务提供者在9090、9091、9092端口
   * 打开浏览器，访问如下地址将触发远程服务调用
   ``` bash
   http://localhost:9000/hello
   ```
   * 消费者将根据配置的注册中心发现远程服务，执行相应的远程服务调用

2. 演示DirectRegistry （远程直连配置）
   - 启动服务提供者，启动命令如下，
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.provider.port=9090 -jar %service_provider_jar%
   java -Drpc.transport.provider.port=9091 -jar %service_provider_jar%
   java -Drpc.transport.provider.port=9092 -jar %service_provider_jar%
   ```
   - 服务消费者，启动命令如下，配置注册中心类型为direct，并指定了直连的远程服务地址。
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   set service_remote=http://localhost:9090/rpc,http://localhost:9091/rpc,http://localhost:9092/rpc
   java -Dserver.port=9000 -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据直连配置，轮询访问三个远程服务提供者。

2. 演示LocalRegistry （远程服务本地调用）
   - 启动服务消费者，启动命令如下，
   ``` bash
   set service_consumer_local_jar=./service-consumer-local/target/service-consumer-local-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9002 -Drpc.registry.type=local -jar %service_consumer_local_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9002/hello
   ```
   刷新页面，观察消费者日志，远程服务的调用将会直接转为本地调用。
   ```
   [20180508 21:03:11-480][http-nio-9002-exec-6] try to visit server...1
   [20180508 21:03:11-480][http-nio-9002-exec-6] consumer send a remote rpc call to http://172.20.16.89:9099/rpc...
   [20180508 21:03:11-482][qtp1335061928-21] receive a remote rpc call in the provider...
   [20180508 21:03:11-484][http-nio-9002-exec-6] consumer receive response from remote rpc call, value=Hello, Michael. This is greetings from localhost:9002 at Tue May 08 21:03:11 CST 2018.
   ```

4. 演示FileRegistry （文件注册中心）
   - 清空文件c://temp/registry.txt，或者删除。
   - 启动服务提供者，启动命令如下，配置注册中心类型为file，在文件registry.txt中实现服务的注册和发现
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.provider.port=9090 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.provider.port=9091 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.provider.port=9092 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   ```
   启动后，可以打开文件c://temp/registry.txt，查看所有服务的注册信息。
   - 服务消费者，启动命令如下，配置注册中心类型为file，在文件registry.txt中发现服务提供者
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9001 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9001/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，轮询访问三个远程服务提供者。
   - 可以尝试先后关闭9090/9091端口的服务提供者，再次刷新页面，可以发现消费者会根据服务在注册中心的状态，选择可用的服务提供者进行调用。

## 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 开源许可协议 License
Apache License 2.0
