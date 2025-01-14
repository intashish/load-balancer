package com.example.loadbalancer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class 	LoadbalancerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadbalancerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(BackendConfig backendConfig) {
		return args -> {
			System.out.println("Backend Servers: " + backendConfig.getBackendServers());
		};
	}
}
