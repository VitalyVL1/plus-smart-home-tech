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
 * <p>
 * Предоставляет бины для:
 * <ul>
 *   <li>Аутентификации через Basic Auth (учетные данные загружаются из конфигурации)</li>
 *   <li>Кастомной обработки ошибок от сервиса оплаты</li>
 * </ul>
 *
 * @see OrderErrorDecoder
 * @see BasicAuthRequestInterceptor
 */
public class OrderFeignClientConfig {
    @Value("${feign.clients.order.username}")
    private String username;
    @Value("${feign.clients.order.password}")
    private String password;

    @Bean
    public RequestInterceptor orderAuthInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder orderErrorDecoder() {
        return new OrderErrorDecoder();
    }
}
