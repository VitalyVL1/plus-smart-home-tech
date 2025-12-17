package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.OrderClient;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.dto.payment.PaymentStatus;
import ru.practicum.exception.NoPaymentFoundException;
import ru.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.mapper.PaymentMapper;
import ru.practicum.model.Payment;
import ru.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    private final TransactionTemplate transactionTemplate;

    private final BigDecimal FEE_PERCENTAGE = BigDecimal.valueOf(0.1);

    @Override
    public PaymentDto payment(OrderDto orderDto) {
        checkOrderDto(orderDto);

        Payment payment = Payment.builder()
                .orderId(orderDto.orderId())
                .totalPayment(orderDto.totalPrice())
                .deliveryTotal(orderDto.deliveryPrice())
                .feeTotal(calculateFee(orderDto.productPrice()))
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        BigDecimal deliveryTotal = orderDto.deliveryPrice();
        BigDecimal productTotal = orderDto.productPrice();

        if (deliveryTotal == null || productTotal == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info in order to calculate cost");
        }

        return deliveryTotal
                .add(productTotal)
                .add(calculateFee(productTotal))
                .setScale(2, RoundingMode.HALF_UP);
    }

    //Согласно open API наименование метода такое
    @Override
    public void refund(UUID paymentId) {
        log.info("Processing payment SUCCESS for paymentId: {}", paymentId);

        Payment payment = transactionTemplate.execute(status -> {
            Payment paymentInProgress = getPayment(paymentId);

            if (paymentInProgress.getPaymentStatus() == PaymentStatus.SUCCESS) {
                log.info("Payment {} already in SUCCESS state", paymentId);
                return paymentInProgress;
            }

            log.debug("Updating payment {} status from {} to SUCCESS",
                    paymentId, paymentInProgress.getPaymentStatus());

            paymentInProgress.setPaymentStatus(PaymentStatus.SUCCESS);
            return paymentInProgress;
        });

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            try {
                log.info("Notifying order service about successful payment for order: {}",
                        payment.getOrderId());
                orderClient.paymentSuccess(payment.getOrderId());
                log.info("Successfully notified order service");
            } catch (Exception e) {
                log.warn("Failed to notify order service about SUCCESSFUL payment: {}",
                        paymentId, e);
            }
        }
    }

    @Override
    public BigDecimal productCost(OrderDto orderDto) {
        Map<UUID, Long> products = orderDto.products();

        if (products == null || products.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("No products in order to calculate cost");
        }

        return products.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
                    BigDecimal price = shoppingStoreClient.getProduct(productId).price();
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void failed(UUID paymentId) {
        Payment payment = transactionTemplate.execute(status -> {
            Payment paymentInProgress = getPayment(paymentId);

            // Если уже в FAIL - ничего не делаем
            if (paymentInProgress.getPaymentStatus() == PaymentStatus.FAIL) {
                log.info("Payment {} already in FAIL state", paymentId);
                return paymentInProgress;
            }

            // Нельзя перейти из SUCCESS в FAIL
            if (paymentInProgress.getPaymentStatus() == PaymentStatus.SUCCESS) {
                log.warn("Attempt to move payment {} from SUCCESS to FAIL", paymentId);
                throw new IllegalStateException("Cannot move payment from SUCCESS to FAIL");
            }

            paymentInProgress.setPaymentStatus(PaymentStatus.FAIL);
            return paymentInProgress;
        });

        if (payment.getOrderId() != null) {
            try {
                orderClient.paymentFailed(payment.getOrderId());
            } catch (Exception e) {
                log.warn("Failed to notify order service about failed payment: {}", paymentId, e);
            }
        }
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findPaymentByPaymentId(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Payment with id = " + paymentId + " not found"));
    }

    private void checkOrderDto(OrderDto orderDto) {
        if (orderDto == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Order is null");
        }
        if (orderDto.deliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery price is null");
        }
        if (orderDto.productPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Product price is null");
        }
        if (orderDto.totalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Total price is null");
        }
    }

    private BigDecimal calculateFee(BigDecimal productPrice) {
        return productPrice.multiply(FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}

