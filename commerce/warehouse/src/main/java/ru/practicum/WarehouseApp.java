package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@Slf4j
public class WarehouseApp {
    public static void main(String[] args) {
        log.info("Starting WarehouseApp");
        SpringApplication.run(WarehouseApp.class, args);
        log.info("WarehouseApp started");
    }
}
