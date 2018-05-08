## simple-rpc
本项目以尽量简单的方式演示一个RPC框架中所使用的核心技术，包括

  * 服务提供方和消费方实现
  * 服务的注册和发现机制
  * 服务集群的高可用和负载均衡
  * 监控治理
  * 远程调用的底层通信实现
  * 数据传输的序列化和反序列化处理
  * 等等。

目标是演示和调研RPC框架技术，将会对每一个话题提供一个演示项目，帮助理解各个RPC核心技术原理和实现。

This project use a simple way as possible to briefly introduce the core technologies which are usually used on a RPC framework, which includes,
  * service provider and consumer
  * service registry and discovery
  * the service cluster's high availability and load balance
  * service monitor
  * the communication protocol of remote service call
  * data serialization and deserialization during data transport
  * etc

There are a series of demo projects, which will introduce the core techs one by one, helps to understand the implementation and theory of the core rpc techs separately.

## 环境准备
准备好JDK7和Maven3即可。

## 一分钟快速入门 Getting Started
请进入演示2的目录，按照说明文档进行项目构建和运行，按照演示步骤执行一次简单的远程调用。

## 演示1
远程调用框架演示，对dubbo,grpc,motan这三个RPC框架的演示使用。

## 演示2
一个简单的远程调用，实现如下的功能
  * 服务的接口、消费方、提供方
  * 框架：注解+消费代理+远程调用+提供接口

## 演示3
集群的负载均衡和高可用，实现如下的功能
  * haproxy
  * load balance

## 演示4
注册中心（registry）

## 演示5
通信协议（Protocol）

## 演示6（TBD）
调用监控（metrics）

## 演示7（TBD）
数据序列化（data serialization）

## 演示（全栈技术）
full pack tech demo

## 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 开源许可协议 License
Apache License 2.0
