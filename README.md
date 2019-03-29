## simple-rpc
本项目以尽量简单的方式演示一个RPC框架中所使用的核心技术，包括

  * 服务提供方和消费方实现
  * 服务的注册和发现机制
  * 服务集群的高可用和负载均衡
  * 远程调用的底层通信实现
  * 数据传输的序列化和反序列化处理
  * 监控治理
  * 等等。

目标是演示和调研RPC框架技术，将会对每一个话题提供一个演示项目，帮助理解各个RPC核心技术原理和实现。

This project use a simple way as possible to briefly introduce the core technologies which are usually used on a RPC framework, which includes,
  * service provider and consumer
  * service registry and discovery
  * the service cluster's high availability and load balance
  * the communication protocol of remote service call
  * data serialization and deserialization during data transport
  * service monitor
  * etc

There are a series of demo projects, which will introduce the core techs one by one, helps to understand the implementation and theory of the core rpc techs separately.

## 环境准备
准备好JDK7和Maven3即可。


## 项目结构简介

整个项目分6个子项目，

- demo-1  业界已有RPC框架的演示使用
- demo-2  演示一个简要RPC框架的搭建
- demo-3  演示一个简要RPC框架里的负载均衡和高可用
- demo-4  演示一个简要RPC框架里的注册中心
- demo-5  演示一个简要RPC框架里的通信协议
- demo-6  演示一个简要RPC框架里的数据序列化（TBD）
- demo-7  演示一个简要RPC框架里的调用监控埋点（TBD）
- demo-full-pack 全栈技术演示

从demo2到demo-full-pack，每一个项目是在前一个项目的代码基础上丰富新的RPC功能，最后的demo-full-pack拥有所有的RPC功能演示。

各个demo项目都独立的子项目，各自独立打开进行编译构建，请从demo-2开始入手查看RPC框架，然后依次打开demo-3到demo-5项目，查看演示的功能，具体演示步骤请参考各个子项目的README文件。

整个项目的构建以demo-full-pack子项目作为最终的全栈技术演示。

## 一分钟快速入门 Getting Started

演示2项目提供了最简单的框架演示，请进入演示目录，按照说明文档进行项目构建和运行，按照演示步骤执行一次简单的远程调用。

## 演示1（demo-1）
远程调用框架演示，对dubbo,grpc,motan这三个RPC框架的演示使用。

## 演示2（demo-2）
一个简单的远程调用，实现如下的功能
  * 服务的接口、消费方、提供方
  * 框架：注解+消费代理+远程调用+提供接口

## 演示3（demo-3）
集群的负载均衡和高可用，实现如下的功能
  * haproxy = failfast, failover
  * load balancer = random, roundrobin

## 演示4（demo-4）
注册中心（registry），实现如下的功能
  * 直连调用（DirectRegistry）
  * 本地调用（LocalRegistry）
  * 文件注册中心（FileRegistry）

## 演示5（demo-5）
通信协议（Protocol），实现如下的功能
  * 基于Jetty的HTTP数据通信
  * 基于Netty的TCP数据通信

## 演示6（TBD）
数据序列化（data serialization），实现如下的功能
  * 简单的Java Object Serialization

## 演示7（TBD）
调用监控（metrics）

## 演示（全栈技术）（demo-full-pack）
全栈技术演示

## 联系 Contact
我们的邮箱地址：peipeihh@qq.com，欢迎来信联系。

## 开源许可协议 License
Apache License 2.0
