package com.epam.xstack.gym.trainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.jms.annotation.EnableJms;

@EnableDiscoveryClient
@EnableJms
@SpringBootApplication
public class TrainerWorkloadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainerWorkloadServiceApplication.class, args);
    }

}
