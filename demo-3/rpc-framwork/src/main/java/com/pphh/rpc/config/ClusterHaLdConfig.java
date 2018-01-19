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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyinhuang on 1/18/2018.
 * Initialize the cluster's HA (high availability) and load balance module on service client side
 */
@Configuration
public class ClusterHaLdConfig {

    @Value("${rpc.client.remote.service:http://localhost:9090/rpc}")
    private String remoteHosts;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.lb", matchIfMissing = true)
    public LoadBalancer buildLoadBalance(@Value("${rpc.client.lb:random}") String lbType) {
        LoadBalancer lb = null;

        List<RemoteService> remoteServices = getListOfRemoteService();
        if (lbType != null && lbType.equals("random")) {
            lb = new RandomLoadBalancer(remoteServices);
        } else if (lbType != null && lbType.equals("roundrobin")) {
            lb = new RoundRobinLoadBalancer(remoteServices);
        }

        return lb;
    }

    private List<RemoteService> getListOfRemoteService() {
        List<RemoteService> remoteServices = new ArrayList<>();
        String[] urls = remoteHosts.split(",");
        for (String url : urls) {
            remoteServices.add(new RemoteService(url));
        }
        return remoteServices;
    }

    @Bean
    @ConditionalOnProperty(name = "rpc.client.ha", matchIfMissing = true)
    public HaStrategy buildFailOverHaStrategy(@Value("${rpc.client.ha:failfast}") String haType) {
        HaStrategy ha = null;

        if (haType.equals("failfast")) {
            ha = new FailFastHaStrategy();
        } else if (haType.equals("failover")) {
            ha = new FailOverHaStrategy();
        }

        return ha;
    }

    @Bean
    @ConditionalOnBean({HaStrategy.class, LoadBalancer.class})
    public ClusterCaller buildClusterCaller(HaStrategy ha, LoadBalancer lb) {
        ClusterCaller cc = new DefaultClusterCaller(ha, lb);
        ProxyInvocationHandler.clusterCaller = cc;
        return cc;
    }

}
