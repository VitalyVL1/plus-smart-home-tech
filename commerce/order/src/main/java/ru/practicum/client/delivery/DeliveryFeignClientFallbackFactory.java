package ru.practicum.client.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ResourceNotFoundException;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Фабрика fallback для Feign клиента доставки.
 * <p>
 * Создает резервную реализацию {@link DeliveryFeignClient},
 * которая вызывается при сбоях в основном сервисе.
 */
@Component
@Slf4j
public class DeliveryFeignClientFallbackFactory implements FallbackFactory<DeliveryFeignClient> {
    @Override
    public DeliveryFeignClient create(Throwable cause) {
        return new DeliveryFeignClient() {
            @Override
            public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
                fastFallBack(cause);
                return null;
            }

            @Override
            public void successfulDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public void pickedDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public void failedDelivery(UUID orderId) {
                fastFallBack(cause);
            }

            @Override
            public BigDecimal deliveryCost(OrderDto orderDto) {
                fastFallBack(cause);
                return null;
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
