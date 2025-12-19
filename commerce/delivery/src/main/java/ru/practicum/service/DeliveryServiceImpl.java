package ru.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.OrderClient;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.delivery.DeliveryState;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.practicum.exception.NoDeliveryFoundException;
import ru.practicum.mapper.DeliveryMapper;
import ru.practicum.model.Delivery;
import ru.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;
    private static final BigDecimal BASE_DELIVETY_RATE = BigDecimal.valueOf(5);

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    @Override
    public void successfulDelivery(UUID orderId) {
        transactionTemplate.executeWithoutResult(status -> {
            Delivery delivery = getDelivery(orderId);
            delivery.setDeliveryState(DeliveryState.DELIVERED);
        });

        try {
            orderClient.delivery(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to send SUCCESS status to orderClient", e);
        }
    }

    @Override
    public void pickedDelivery(UUID orderId) {
        Delivery delivery = transactionTemplate.execute(status -> {
            Delivery deliveryInProgress = getDelivery(orderId);
            deliveryInProgress.setDeliveryState(DeliveryState.IN_PROGRESS);
            return deliveryInProgress;
        });

        ShippedToDeliveryRequest request =
                new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId());

        try {
            warehouseClient.shippedToDelivery(request);
        } catch (Exception e) {
            log.warn("Error while trying to send delivery request to warehouseClient", e);
        }

        try {
            orderClient.assemblyOrder(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to set Assembled status to orderClient", e);
        }

    }

    @Override
    public void failedDelivery(UUID orderId) {
        transactionTemplate.executeWithoutResult(status -> {
            Delivery delivery = getDelivery(orderId);
            delivery.setDeliveryState(DeliveryState.FAILED);
        });

        try {
            orderClient.deliveryFailed(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to send FAILED status to orderClient", e);
        }
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        Delivery delivery = getDelivery(orderDto.orderId());
        String fromStreet = delivery.getFromAddress().getStreet();
        String toStreet = delivery.getToAddress().getStreet();

        BigDecimal cost = BASE_DELIVETY_RATE;

        // 1. Проверяем адрес склада, если адрес склада ADDRESS_2,
        // то базовую стоимость умножаем на 2 и прибавляем базовую стоимость
        // другими словами базовую стоимость умножаем на 3
        if (fromStreet.equalsIgnoreCase("ADDRESS_2")) {
            cost = cost.multiply(BigDecimal.valueOf(3));
        }

        // 2. Если хрупкий, до добавляем 20% к стоимости доставки
        if (orderDto.fragile()) {
            cost = cost.multiply(BigDecimal.valueOf(1.2));
        }

        // 3. Добавляем стоимость доставки по весу и объему
        BigDecimal weightCost = BigDecimal.valueOf(orderDto.deliveryWeight())
                .multiply(BigDecimal.valueOf(0.3));

        BigDecimal volumeCost = BigDecimal.valueOf(orderDto.deliveryVolume())
                .multiply(BigDecimal.valueOf(0.2));

        cost = cost.add(weightCost).add(volumeCost);

        // 4. если улица склада и улица доставки не совпадают то добавляем 20% к стоимости доставки
        if (!fromStreet.equalsIgnoreCase(toStreet)) {
            cost = cost.multiply(BigDecimal.valueOf(1.2));
        }

        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void cancelDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() ->
                        new NoDeliveryFoundException("Delivery for deliveryId = " + deliveryId + " not found"));

        if (delivery.getDeliveryState().equals(DeliveryState.CANCELLED)) {
            log.info("Delivery for deliveryId = " + deliveryId + " is already cancelled");
        }

        delivery.setDeliveryState(DeliveryState.CANCELLED);
    }

    private Delivery getDelivery(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery for orderId = " + orderId + " not found"));
    }
}
