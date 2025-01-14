package com.example.loadbalancer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {
    private final List<String> backendServers;
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final RestTemplate restTemplate;

    public LoadBalancerService(BackendConfig backendConfig){
        this.backendServers = backendConfig.getBackendServers();
        this.restTemplate = new RestTemplate();
    }

    // This method will handle the load balancing logic
    public String forwardRequest(String path){
        int index = currentIndex.getAndUpdate(i -> (i + 1) % backendServers.size());
        String backendUrl = backendServers.get(index) + path;
        return restTemplate.getForObject(backendUrl, String.class);
    }
}
