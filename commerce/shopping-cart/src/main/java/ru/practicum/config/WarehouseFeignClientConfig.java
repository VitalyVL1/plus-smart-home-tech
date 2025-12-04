package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.WarehouseErrorDecoder;

@Configuration
public class WarehouseFeignClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new WarehouseErrorDecoder();
    }
}
