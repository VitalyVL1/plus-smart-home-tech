package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Главный класс сервиса gateway.
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class Gateway {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        log.info("Starting Gateway");
        SpringApplication.run(Gateway.class, args);
        log.info("Gateway started");
    }
}
