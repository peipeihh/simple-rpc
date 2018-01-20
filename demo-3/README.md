
## 说明

本项目演示对远程服务调用中的HA容错策略和负载均衡机制。

远程服务调用中，容错策略一般有如下几种，
- FailFast 快速失败：当消费者调用远程服务失败时，立即报错，消费者只发起一次调用请求。
- FailOver 失败自动切换：当消费者调用远程服务失败时，重新尝试调用服务，重试的次数一般需要指定，防止无限次重试。
- FailSafe 失败安全：当消费者调用远程服务失败时，直接忽略，请求正常返回报成功。一般用于可有可无的服务调用。
- FailBack 失败自动恢复：当消费者调用远程服务失败时，定时重发请求。一般用于消息通知。
- Forking 并行调用：消费者同时调用多个远程服务，任一成功响应则返回。

负载均衡机制解决的是如何在多个可用远程服务提供者中，选择下一个进行调用，主要有如下选择策略，
- Random 随机选择：在可用的远程服务提供者中，随机选择一个进行调用。
- RoundRobin 轮询选择：在可用的远程服务提供者中，依次轮询选择一个进行调用。
- LeastActive 按最少活跃调用数选择：在可用的远程服务提供者中，选择最少调用过的远程进行调用。
- ConsistentHash 按一致哈希选择：获取调用请求的哈希值，根据哈希值选择远程服务提供者，保证相同参数的请求总是发到同一提供者。

在本项目中主要实现了failfast和failover的容错策略，random和RoundRobin的负载均衡机制。服务消费端可以通过如下两个环境属性来配置策略，
- rpc.client.ha = failfast, failover
- rpc.client.lb = random, roundrobin

更多详细请见下面的演示。

## 项目编译
整个项目目录结构如下，
```
- pom.xml 整个Maven项目的pom文件，这是演示项目的根目录
+ demo-scripts 演示的启动脚本命令
+ rpc-framework
  - pom.xml
  + src
    + main/java/com/pphh/rpc/cluster
      + ha
        - FailFastHaStrategy
        - FailOverHaStrategy
      + lb
        - RandomLoadBalancer
        - RoundRobinLoadBalancer
      + support
      - ClusterCaller
      - HaStrategy
      - LoadBalancer
+ service-api
  - pom.xml
  + src
+ service-consumer
  - pom.xml
  + src
+ service-provider
  - pom.xml
  + src
```
其中集群的容错策略和负载均衡实现在rpc-framework子项目中的com.pphh.rpc.cluster包中。

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
   * 消费者将根据指定的容错策略和负载均衡机制，执行相应的远程服务调用

2. 缺省配置启动（failfast容错策略和random负载均衡机制）
   - 打开一个shell窗口，运行服务消费者，启动端口为9000，并配置可用的远程服务地址。
   ``` bash
   set service_consumer_jar=./service-consumer/target/service-consumer-v3-1.3-SNAPSHOT.jar
   set service_remote=http://localhost:9090/rpc,http://localhost:9091/rpc,http://localhost:9092/rpc
   java -Dserver.port=9000 -Drpc.client.remote.service=%service_remote% -jar %service_consumer_jar%
   ```
   在默认情况下（未指定rpc.client.ha和rpc.client.lb环境属性），消费者使用failfast容错策略和random负载均衡机制。
   上述命令等同于，
   ``` bash
   java -Dserver.port=9000 -Drpc.client.ha=failfast -Drpc.client.lb=random -Drpc.client.remote.service=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开三个shell窗口，分别运行服务提供者在端口9090、9091和9092（请在三个不同shell中启动java）。
   ``` bash
   set service_provider_jar=./service-provider/target/service-provider-v3-1.3-SNAPSHOT.jar
   java -Dserver.port=9090 -jar %service_provider_jar%
   java -Dserver.port=9091 -jar %service_provider_jar%
   java -Dserver.port=9092 -jar %service_provider_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新多次可以发现，远程服务的调用是随机访问。

3. 演示服务的负载均衡机制 - roundrobin （轮询选择可用远程服务）
   - 重启服务消费者，启动命令如下，配置loadbalance策略为roundrobin。
   ``` bash
   java -Dserver.port=9000 -Drpc.client.lb=roundrobin -Drpc.client.remote.service=%service_remote% -jar %service_consumer_jar%
   ```
   - 打开浏览器，访问如下地址，刷新页面可以看到远程调用请求成功后的消息。
   ``` bash
   http://localhost:9000/hello
   ```
   刷新多次可以发现，远程服务的调用是依次对9090、9091和9092端口进行访问。这就是轮询选择可用远程服务。

4. 演示服务的容错策略 - failfast（快速失败）和 failover（失败自动切换）
   - 为了演示容错策略，可以将9092端口的服务提供者关停（在shell窗口中按Ctrl+C）。
   - 重启服务消费者，启动命令如下，配置容错策略为failfast。
   ``` bash
   java -Dserver.port=9000 -Drpc.client.ha=failfast -Drpc.client.remote.service=%service_remote% -jar %service_consumer_jar%
   ```
   刷新多次可以发现，当远程服务调用到9092端口时，服务抛出异常，页面会报错。
   - 再次重启服务消费者，启动命令如下，配置容错策略为failover。
   ``` bash
   java -Dserver.port=9000 -Drpc.client.ha=failover -Drpc.client.remote.service=%service_remote% -jar %service_consumer_jar%
   ```
   刷新多次可以发现，当远程服务调用到9092端口时，服务抛出异常，但马上会重试远程调用，直到成功，重试次数为3次。这就是失败自动切换的机制在起作用。
