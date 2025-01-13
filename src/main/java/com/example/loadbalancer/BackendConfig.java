package com.example.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BackendConfig {

    @Value("#{'${backend.servers}'.split(',')}")
    private List<String> backendServers;

    public List<String> getBackendServers(){
        return backendServers;
    }

}
