
## 说明

本项目演示几个业界开源的RPC（远程服务调用）框架的简单使用，
- 新浪微博的[motan](https://github.com/weibocom/motan)
- 淘宝的[dubbo](http://dubbo.io)
- 谷歌的[gRpc](https://grpc.io/)

各个RPC框架的特点和简单比较见如下表
- TODO



## 项目编译
整个项目目录结构如下，
```
- pom.xml 整个Maven项目的pom文件，这是演示项目的根目录
+ demo-scripts 演示的启动脚本命令
+ dubbon
  - pom.xml
  + dubbon-demo-api
  + dubbon-demo-client
  + dubbon-demo-server
+ motan
  - pom.xml
  + motan-demo-api
  + motan-demo-client
  + motan-demo-server
+ gRpc
```

其中，
- dubbo的演示代码参考其github上的[quickstart](https://dubbo.gitbooks.io/dubbo-user-book/content/quick-start.html)
- motan的演示代码参考其github上的[quickstart](https://github.com/weibocom/motan/wiki/en_quickstart)
- gRpc的演示代码参考其[quickstart](https://grpc.io/docs/quickstart/java.html)

请打开shell窗口，切换在演示项目的根目录中，执行如下命令，对项目编译打包，
``` bash
mvn clean package
```

## 演示环境

若无特别声明，演示的shell命令都将在演示项目的根目录中执行。
环境：Windows 7 SP1 + Java 9.0.1 + Maven 3.3.9

## 演示

1. 使用motan演示远程服务调用

   * 启动服务Server端
   ``` bash
   set motan_server_jar=./motan/motan-demo-client/target/motan-demo-server-1.0-SNAPSHOT.jar
   java -jar %motan_server_jar%
   ```
   * 启动服务客户端
   ``` bash
   set motan_client_jar=./motan/motan-demo-client/target/motan-demo-client-1.0-SNAPSHOT.jar
   java -jar %motan_client_jar%
   ```
   * 若启动正常，将看到正常的远程服务调用。
   客户端日志
   ``` bash
   hello motan0
   hello motan1
   hello motan2
   ```
   服务端日志
   ``` bash
   motan0 invoked rpc service
   motan1 invoked rpc service
   motan2 invoked rpc service
   ```

2. 使用dubbo演示远程服务调用
   * 启动服务Server端
   ``` bash
   set dubbo_provider_jar=./dubbo/dubbo-demo-provider/target/dubbo-demo-provider-1.0-SNAPSHOT.jar
   java -jar %dubbo_provider_jar%
   ```
   * 启动服务客户端
   ``` bash
   set dubbo_consumer_jar=./dubbo/dubbo-demo-consumer/target/dubbo-demo-consumer-1.0-SNAPSHOT.jar
   java -jar %dubbo_consumer_jar%
   ```
   * 若启动正常，将看到正常的远程服务调用。
   客户端日志
   ``` bash
   Hello world
   Hello world
   Hello world
   ```

3. 使用gRpc演示远程服务调用
   * 启动服务Server端
   ``` bash
   cd ./grpc/
   mvn -Dexec.mainClass="io.grpc.HelloWorldServer" exec:java
   ```
   * 启动服务Client端
   ``` bash
   cd ./grpc/
   mvn -Dexec.mainClass="io.grpc.HelloWorldClient" exec:java
   ```
   * 若启动正常，将看到正常的远程服务调用，日志见如下，
   ```
   Will try to greet world ...
   Greeting: Hello world
   Will try to greet world ...
   Greeting: Hello world
   ```
