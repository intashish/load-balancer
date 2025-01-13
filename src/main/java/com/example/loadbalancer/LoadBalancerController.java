package com.example.loadbalancer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/load-balancer")
public class LoadBalancerController {

    @GetMapping("/status")
    public String status(){
        return "load balancer is running";
    }
}
