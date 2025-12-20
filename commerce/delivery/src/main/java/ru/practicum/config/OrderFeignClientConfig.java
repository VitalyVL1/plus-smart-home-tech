package ru.practicum.config;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.practicum.client.order.OrderErrorDecoder;

/**
 * Конфигурация для Feign клиента заказов.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом заказов через Feign.
 */
public class OrderFeignClientConfig {

    @Value("${feign.clients.order.username}")
    private String username;

    @Value("${feign.clients.order.password}")
    private String password;

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

    @Bean
    public RequestInterceptor orderAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
