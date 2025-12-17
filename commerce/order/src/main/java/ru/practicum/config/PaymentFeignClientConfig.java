package ru.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.payment.PaymentErrorDecoder;

/**
 * Конфигурация для Feign клиента оплаты.
 * <p>
 * Настраивает компоненты, специфичные для взаимодействия
 * с микросервисом оплаты через Feign.
 */
@Configuration
public class PaymentFeignClientConfig {
    /**
     * Создает декодер ошибок для Feign клиента оплаты.
     * <p>
     * Декодер обрабатывает HTTP ответы от сервиса оплаты
     * и преобразует их в соответствующие исключения.
     *
     * @return декодер ошибок
     */
    @Bean
    public ErrorDecoder paymentErrorDecoder() {
        return new PaymentErrorDecoder();
    }
}
