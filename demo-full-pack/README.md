
## 说明

本项目演示远程服务调用的注册中心组件。

远程服务调用中，注册中心是一个寻找远程服务提供者的信息中心，若将远程服务通信比喻成打电话，那么注册中心就像一个电话簿，电话簿可以告诉联系人的电话号码，而注册中心能告诉远程服务提供者的IP地址。

注册中心主要包含如下三个功能，
- 发现服务：通过服务名查询远程服务提供者的URL地址列表
- 注册服务：供服务提供者注册使用，使得服务消费者发现
- 注销服务：供服务提供者注销使用，以便停止服务调用

根据注册中心实现的机制和工具不同，一般来说可以有如下几种，
- Local 本地调用：服务消费者本身也是提供者，将远程调用转为本地调用，无需服务注册和注销。
- Direct 远程直连调用：远程服务地址为配置的URL地址，无需服务注册和注销。
- File 文件注册中心：通过文件实现服务的注册、注销和发现。
- Redis 注册中心：通过redis实现服务的注册、注销和发现。

可以通过下表来看不同实现方式的特性，

|  | 发现服务 | 注册服务 | 注销服务  |
| ---: | --- | --- | --- |
| Local  | 本地调用  | 无 | 无  |
| Direct | 通过配置 | 无 | 无   |
| File | 通过文件 | 有，注册服务到文件上 | 有   |
| Redis  | 通过redis | 有，注册服务到redis上 | 有  |


在本项目中主要实现了local，direct和file三种类型的注册中心，服务提供端和消费端可以通过如下三个环境属性来对注册中心进行配置，
- rpc.registry.type = local, direct, file
- rpc.registry.host = http://registry
- rpc.registry.direct.remote = http://localhost:9090/rpc

其中type是注册中心的类型，host是注册中心的地址，direct.remote是服务直连时配置远程服务URL地址。

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
        - RegistryConfig 注册中心的配置
        - ClusterHaLdConfig 注册中心对接clusterCaller
      + registry
        - Registry
        - LocalRegistry  本地调用
        - DirectRegistry 远程直连调用
        - FileRegistry 文件注册中心
+ service-api
  - pom.xml
  + src
+ service-consumer-local 演示注册中心的本地连接
  - pom.xml
  + src
+ service-consumer 演示注册中心的按配置进行远程连接
  - pom.xml
  + src
+ service-provider
  - pom.xml
  + src
```
其中注册中心的实现在rpc-framework子项目中的com.pphh.rpc.registry中。

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
   java -Dserver.port=9090 -jar %service_provider_jar%
   java -Dserver.port=9091 -jar %service_provider_jar%
   java -Dserver.port=9092 -jar %service_provider_jar%
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
   [20180227 10:36:36-738][http-nio-9001-exec-3] consumer send a remote rpc call to http://172.20.16.89:9002/rpc...
   [20180227 10:36:36-740][http-nio-9001-exec-4] receive a remote rpc call in the provider...
   [20180227 10:36:36-742][http-nio-9001-exec-3] consumer receive response from remote rpc call, value=Hello, Michael. This is greetings from localhost:9001 at Tue Feb 27 10:36:36 CST 2018.
   ```

4. 演示FileRegistry （文件注册中心）
   - 清空文件c://temp/registry.txt，或者删除。
   - 启动服务提供者，启动命令如下，配置注册中心类型为file，在文件registry.txt中实现服务的注册和发现
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9090 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Dserver.port=9091 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   java -Dserver.port=9092 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_provider_jar%
   ```
   启动后，可以打开文件c://temp/registry.txt，查看所有服务的注册信息。
   - 服务消费者，启动命令如下，配置注册中心类型为file，在文件registry.txt中发现服务提供者
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v10-1.10-SNAPSHOT.jar
   java -Dserver.port=9000 -Drpc.registry.type=file -Drpc.registry.host="c://temp/registry.txt" -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新页面，观察消费者和提供者的日志，远程服务的调用根据文件注册中心，轮询访问三个远程服务提供者。

## 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 开源许可协议 License
Apache License 2.0