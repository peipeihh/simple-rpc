package com.pphh.rpc.config;

import com.pphh.rpc.cluster.ClusterCaller;
import com.pphh.rpc.cluster.HaStrategy;
import com.pphh.rpc.cluster.LoadBalancer;
import com.pphh.rpc.cluster.ha.FailFastHaStrategy;
import com.pphh.rpc.cluster.ha.FailOverHaStrategy;
import com.pphh.rpc.cluster.lb.RandomLoadBalancer;
import com.pphh.rpc.cluster.lb.RoundRobinLoadBalancer;
import com.pphh.rpc.cluster.support.DefaultClusterCaller;
import com.pphh.rpc.proxy.ProxyInvocationHandler;
import com.pphh.rpc.transport.RemoteService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyinhuang on 1/18/2018.
 * Initialize the cluster's HA (high availability) and load balance module on service client side
 */
@Configuration
public class ClusterHaLdConfig {

    @Bean
    @ConditionalOnProperty(name = "rpc.client.lb", havingValue = "random", matchIfMissing = true)
    public LoadBalancer buildRandomLoadBalance() {
        List<RemoteService> remoteServices = getListOfRemoteService();
        return new RandomLoadBalancer(remoteServices);
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.lb", havingValue = "roundrobin")
    public LoadBalancer buildRRLoadBalance() {
        List<RemoteService> remoteServices = getListOfRemoteService();
        return new RoundRobinLoadBalancer(remoteServices);
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.ha", havingValue = "failfast", matchIfMissing = true)
    public HaStrategy buildFailFastHaStrategy() {
        return new FailFastHaStrategy();
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.ha", havingValue = "failover")
    public HaStrategy buildFailOverHaStrategy() {
        return new FailOverHaStrategy();
    }

    @Bean
    @ConditionalOnBean({HaStrategy.class, LoadBalancer.class})
    public ClusterCaller buildClusterCaller(HaStrategy ha, LoadBalancer lb) {
        ClusterCaller cc = new DefaultClusterCaller(ha, lb);
        ProxyInvocationHandler.clusterCaller = cc;
        return cc;
    }

    private List<RemoteService> getListOfRemoteService() {
        List<RemoteService> remoteServices = new ArrayList<>();
        remoteServices.add(new RemoteService("http://localhost:8081/rpc"));
        remoteServices.add(new RemoteService("http://localhost:8080/rpc"));
        return remoteServices;
    }

}
