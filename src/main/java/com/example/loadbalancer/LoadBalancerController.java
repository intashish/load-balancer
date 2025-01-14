package com.example.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
@RequestMapping("/load-balancer")
public class LoadBalancerController {

    private final LoadBalancerService loadBalancerService;

    @Autowired
    public LoadBalancerController(LoadBalancerService loadBalancerService){
        this.loadBalancerService = loadBalancerService;
    }

    // Endpoint  to forward request to backend servers.
    @GetMapping("/forward/{path}")
    public String forwardRequest(@PathVariable String path){
        return loadBalancerService.forwardRequest("/" + path);
    }

    @GetMapping("/status")
    public String status(){
        return "load balancer is running";
    }
}
