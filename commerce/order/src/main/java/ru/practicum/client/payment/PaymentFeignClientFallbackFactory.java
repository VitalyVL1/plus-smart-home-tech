package ru.practicum.client.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ResourceNotFoundException;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Фабрика fallback для Feign клиента оплаты.
 * <p>
 * Создает резервную реализацию {@link PaymentFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class PaymentFeignClientFallbackFactory implements FallbackFactory<PaymentFeignClient> {
    @Override
    public PaymentFeignClient create(Throwable cause) {
        return new PaymentFeignClient() {
            @Override
            public PaymentDto payment(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public BigDecimal getTotalCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void refund(UUID paymentId) {
                fastFallBack(cause);
            }

            @Override
            public BigDecimal productCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void failed(UUID paymentId) {
                fastFallBack(cause);
            }

            private void fastFallBack(Throwable cause) {
                if (cause instanceof ResourceNotFoundException) {
                    log.warn("Not found (404): ", cause);
                    throw (ResourceNotFoundException) cause;
                }

                if (cause instanceof BadRequestException) {
                    log.warn("Bad request (4xx): ", cause);
                    throw (BadRequestException) cause;
                }

                log.error("Server/network error ", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }
        };
    }
}
