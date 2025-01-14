package com.example.loadbalancer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {
    private final List<String> backendServers;
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final RestTemplate restTemplate;
    private final Map<String, Boolean> serverHealth = new ConcurrentHashMap<>();

    public LoadBalancerService(BackendConfig backendConfig){
        this.backendServers = backendConfig.getBackendServers();
        this.restTemplate = new RestTemplate();
        this.initHealthCheck();
    }

    // Initial periodic health checks
    private  void initHealthCheck(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for(String server : backendServers){
                try{
                    restTemplate.getForObject(server+"/user/health", String.class);
                    serverHealth.put(server,true); //server is healthy
                } catch (Exception e){
                    System.out.println(e.getStackTrace());
                    serverHealth.put(server, false);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    // Get the next healthy server
    private Optional<String> getNextHealthyServer(){
        int attempts = 0;
        while (attempts < backendServers.size()){
            int index = currentIndex.getAndUpdate(i -> (i + 1) % backendServers.size());
            String server = backendServers.get(index);
            if(serverHealth.getOrDefault(server, false)){
                return  Optional.of(server);
            }
            attempts++;
        }
        return Optional.empty();
    }

    // This method will handle the load balancing logic
    public String forwardRequest(String path){
        Optional<String> server = getNextHealthyServer();
        System.out.println(server);
        if(server.isPresent()){
            String backendUrl = server.get() + path;
            return restTemplate.getForObject(backendUrl, String.class);
        }
//        throw new IllegalStateException("No healthy servers available.");
        return "503 Service Unavailable: No healthy servers available.";
    }
}
