package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.order.OrderErrorDecoder;

/**
 * Конфигурация для Feign клиента заказов.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом заказов через Feign.
 */
@Configuration
public class OrderFeignClientConfig {
    /**
     * Создает декодер ошибок для Feign клиента заказов.
     * <p>
     * Декодер обрабатывает HTTP ответы от сервиса заказов
     * и преобразует их в соответствующие исключения.
     *
     * @return декодер ошибок
     */
    @Bean
    public ErrorDecoder orderErrorDecoder() {
        return new OrderErrorDecoder();
    }
}
