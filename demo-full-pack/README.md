
## 说明

本项目演示远程服务调用的全栈技术演示，主要包括，
- 服务接口的定义和实现，包括服务提供方和消费方的注解代码
- 集群的负载均衡（random、roundrobin）和高可用（failfast、failover）
- 注册中心（DirectRegistry、LocalRegistry、FileRegistry）
- 通信协议（http/tcp）
- 数据序列化（data serialization）

项目中有如下相关的配置，

- 服务提供方
```
# 通信协议（http/tcp）
rpc.transport.type = http, netty  
rpc.transport.provider.port = 9090

# 注册中心 - FileRegistry
rpc.registry.type=file
rpc.registry.host=c:/temp/registry.txt

# 注册中心 - 服务心跳更新
rpc.service.heartbeat.report.enable=true
rpc.service.heartbeat.report.initialDelay=5000
rpc.service.heartbeat.report.fixedRate=5000
```

- 服务消费方
```
# 负载均衡和高可用策略
rpc.client.ha = failover, failfast
rpc.client.lb = random, roundrobin

# 注册中心 - DirectRegistry
rpc.registry.type=direct
rpc.registry.host=
rpc.registry.direct.remote=http://localhost:9090

# 注册中心 - FileRegistry
rpc.registry.type=file
rpc.registry.host=c:/temp/registry.txt

# 注册中心 - LocalRegistry
rpc.registry.type=local

# 通信协议（http/tcp）
rpc.transport.type=http, netty
```

上述把所有的RPC配置列出，在使用中会根据需求进行相应的配置，关于配置具体使用方法请参考演示步骤。


## 项目编译
整个项目目录结构如下，
```
- pom.xml 整个Maven项目的pom文件，这是演示项目的根目录
+ demo-scripts 演示的启动脚本命令
+ rpc-framework
  - pom.xml
  + src
    + main/java/com/pphh/rpc
      + config RPC服务的初始化配置
      + rpc
      + provider 服务提供方的服务接口层
      + proxy 服务消费方的代理层
      + cluster 服务集群的高可用和负载均衡
        + ha/lb
      + registry 服务注册中心
      + scheduler 通信服务接口的定时更新
      + transport  基于Jetty/Netty的HTTP/TCP通信实现
        + http/netty
      + serializer 数据序列化
+ service-api 演示样例-服务接口声明
+ service-consumer-local 演示样例-服务的本地调用
+ service-consumer 演示样例-服务消费方
+ service-provider 演示样例-服务提供者
```

请打开shell窗口，切换在演示项目的根目录中，执行如下命令，对项目编译打包，
``` bash
mvn clean package
```

## 演示环境

若无特别声明，演示的shell命令都将在演示项目的根目录中执行。
环境：Windows 7 SP1 + Java 9.0.1 + Maven 3.3.9

根据不同的注册中心和负载均衡和高可用策略，有如下几种组合演示
|  负载均衡+高可用策略 | failfast + random | failover + roundrobin |
| :------------: | :------------: | :------------: |
| 直连调用（DirectRegistry） | 演示1 | 演示2  |
| 文件注册中心（FileRegistry）  | 演示3  | 演示4  |
| 本地调用（LocalRegistry）  | 无 | 无  |
注：对于本地调用，无需配置高可用和负载策略。

根据不同的注册中心和服务通信，有如下几种组合演示

|   | 基于Jetty的HTTP数据通信 | 基于Netty的TCP数据通信 |
| :------------: | :------------: | :------------: |
| 直连调用（DirectRegistry） | 演示5 | 演示6  |
| 本地调用（LocalRegistry）  | 演示7 | 演示8 |
| 文件注册中心（FileRegistry）  | 演示9  | 演示10  |

各个演示的详细步骤见如下。

## 演示

1. 下面的演示中将分别启动服务消费者和提供者在如下端口，
   * 运行服务消费者在9000端口
   * 运行服务提供者在9090、9091、9092端口
   * 打开浏览器，访问如下地址将触发远程服务调用
   ``` bash
   http://localhost:9000/hello
   ```
   * 消费者将根据配置的注册中心发现远程服务，执行相应的远程服务调用

2. 演示1：failfast + random + DirectRegistry
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
   set service_remote=http://localhost:9090,http://localhost:9091,http://localhost:9092
   java -Dserver.port=9000 -Drpc.client.ha=failfast -Drpc.client.lb=random -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据直连配置，随机访问三个远程服务提供者。
   - 可以尝试关闭服务9090/9091/9092三者之一，然后刷新页面，一旦失败，则直接报错，这是failfast所定义的调用策略。

3. 演示2：failover + roundrobin + DirectRegistry
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
   set service_remote=http://localhost:9090,http://localhost:9091,http://localhost:9092
   java -Dserver.port=9000 -Drpc.client.ha=failover -Drpc.client.lb=roundrobin -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据直连配置，轮询访问三个远程服务提供者。
   - 可以尝试关闭服务9090/9091/9092三者之一，然后刷新页面，一旦失败，则直接会再次尝试发起调用，这是failover所定义的调用策略。下面为访问日志，
   ```
   [20180511 11:11:37-252][http-nio-9000-exec-7] try to visit server...1
   [20180511 11:11:37-253][http-nio-9000-exec-7] consumer send a remote rpc call to http://localhost:9091...
   [20180511 11:11:39-278][http-nio-9000-exec-7] Receive an exception on FailOverStrategy, retry with continue...
   [20180511 11:11:39-278][http-nio-9000-exec-7] try to visit server...2
   [20180511 11:11:39-278][http-nio-9000-exec-7] consumer send a remote rpc call to http://localhost:9092...
   [20180511 11:11:39-282][http-nio-9000-exec-7] consumer receive response from remote rpc call, value=Hello, Michael. This is greetings from localhost:9092 at Fri May 11 11:11:39 CST 2018.
   ```

4. 演示3：failfast + random + FileRegistry
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
   java -Dserver.port=9001 -Drpc.client.ha=failfast -Drpc.client.lb=random -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9001/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，随机访问三个远程服务提供者。
   - 可以尝试先后关闭9090/9091/9092端口的服务提供者，然后刷新页面，可以发现一旦服务被关闭，则客户端可以根据注册中心获知其关闭状态，然后不再访问调用。

5. 演示4：failover + roundrobin + FileRegistry
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
   java -Dserver.port=9001 -Drpc.client.ha=failover -Drpc.client.lb=roundrobin -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9001/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，轮询访问三个远程服务提供者。
   - 可以尝试先后关闭9090/9091/9092端口的服务提供者，然后刷新页面，可以发现一旦服务被关闭，则客户端可以根据注册中心获知其关闭状态，然后不再访问调用。

6. 演示5：基于Jetty的HTTP数据通信 + DirectRegistry
   - 启动服务提供者，启动命令如下，
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9090 -jar %service_provider_jar%
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9091 -jar %service_provider_jar%
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9092 -jar %service_provider_jar%
   ```
   - 服务消费者，启动命令如下，配置注册中心类型为direct，并指定了直连的远程服务地址。
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   set service_remote=http://localhost:9090,http://localhost:9091,http://localhost:9092
   java -Dserver.port=9000  -Drpc.transport.type=http -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据直连配置，轮询访问三个远程服务提供者。

7. 演示6：基于Netty的TCP数据通信 + DirectRegistry
   - 启动服务提供者，启动命令如下，
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9090 -jar %service_provider_jar%
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9091 -jar %service_provider_jar%
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9092 -jar %service_provider_jar%
   ```
   - 服务消费者，启动命令如下，配置注册中心类型为direct，并指定了直连的远程服务地址。
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   set service_remote=netty://localhost:9090,netty://localhost:9091,netty://localhost:9092
   java -Dserver.port=9000  -Drpc.transport.type=netty -Drpc.registry.type=direct -Drpc.registry.direct.remote=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据直连配置，轮询访问三个远程服务提供者。

8. 演示7：基于Jetty的HTTP数据通信 + LocalRegistry
   - 启动服务消费者，启动命令如下，
   ``` bash
   set service_consumer_local_jar=./service-consumer-local/target/service-consumer-local-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9002 -Drpc.transport.type=http -Drpc.registry.type=local -jar %service_consumer_local_jar%
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

9. 演示8：基于Netty的TCP数据通信 + LocalRegistry
   - 启动服务消费者，启动命令如下，
   ``` bash
   set service_consumer_local_jar=./service-consumer-local/target/service-consumer-local-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9002 -Drpc.transport.type=netty -Drpc.registry.type=local -jar %service_consumer_local_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9002/hello
   ```

10. 演示9：基于Jetty的HTTP数据通信 + FileRegistry
   - 清空文件c://temp/registry.txt，或者删除。
   - 启动服务提供者，启动命令如下，配置注册中心类型为file，在文件registry.txt中实现服务的注册和发现
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9090 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9091 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.type=http -Drpc.transport.provider.port=9092 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   ```
   启动后，可以打开文件c://temp/registry.txt，查看所有服务的注册信息。
   - 服务消费者，启动命令如下，配置注册中心类型为file，在文件registry.txt中发现服务提供者
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9001 -Drpc.transport.type=http -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9001/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，轮询访问三个远程服务提供者。
   - 可以尝试先后关闭9090/9091端口的服务提供者，再次刷新页面，可以发现消费者会根据服务在注册中心的状态，选择可用的服务提供者进行调用。

11. 演示10：基于Netty的TCP数据通信 + FileRegistry
   - 清空文件c://temp/registry.txt，或者删除。
   - 启动服务提供者，启动命令如下，配置注册中心类型为file，在文件registry.txt中实现服务的注册和发现
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9090 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9091 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Drpc.transport.type=netty -Drpc.transport.provider.port=9092 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   ```
   启动后，可以打开文件c://temp/registry.txt，查看所有服务的注册信息。
   - 服务消费者，启动命令如下，配置注册中心类型为file，在文件registry.txt中发现服务提供者
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9001 -Drpc.transport.type=netty -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9001/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，轮询访问三个远程服务提供者。

## 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 开源许可协议 License
Apache License 2.0
