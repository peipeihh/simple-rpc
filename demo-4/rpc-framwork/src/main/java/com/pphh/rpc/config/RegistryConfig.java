package com.pphh.rpc.config;

import com.pphh.rpc.registry.*;
import com.pphh.rpc.rpc.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mh on 2018/2/18.
 * Initialize the service registry
 */
@Configuration
public class RegistryConfig {

    @Value("${rpc.registry.type:direct}")
    private String registryType;

    @Value("${rpc.registry.host:}")
    private String registryHost;

    @Value("${rpc.registry.direct.remote:http://localhost:9090/rpc}")
    private String directRemoteHosts;

    @Bean
    @ConditionalOnProperty(name = "rpc.registry.name", matchIfMissing = true)
    public Registry buildRegistry() {
        Registry registry = null;

        switch (registryType) {
            case "direct":
                Set<URL> providers = new HashSet<>();
                String[] urls = directRemoteHosts.split(",");
                for (String url : urls) {
                    URL provider = URL.valueOf(url);
                    if (provider != null) {
                        providers.add(provider);
                    }
                }
                registry = new DirectRegistry(providers);
                break;
            case "local":
                registry = new LocalRegistry();
                break;
            case "redis":
                registry = new RedisRegistry(registryHost);
                break;
            case "file":
                registry = new FileRegistry(registryHost);
                break;
        }

        return registry;
    }

}
