package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.delivery.DeliveryErrorDecoder;

/**
 * Конфигурация для Feign клиента доставки.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом доставки через Feign.
 */
@Configuration
public class DeliveryFeignClientConfig {
    /**
     * Создает декодер ошибок для Feign клиента оплаты.
     * <p>
     * Декодер обрабатывает HTTP ответы от сервиса оплаты
     * и преобразует их в соответствующие исключения.
     *
     * @return декодер ошибок
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new DeliveryErrorDecoder();
    }
}
