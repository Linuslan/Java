package com.craftsman.thirteenbasicservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = "com.craftsman")
@EnableFeignClients(basePackages = "com.craftsman")
@EnableDiscoveryClient
public class ThirteenBasicServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirteenBasicServiceApplication.class, args);
    }
}
