package com.fleexy.aclgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AclGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclGatewayApplication.class, args);
    }
}
