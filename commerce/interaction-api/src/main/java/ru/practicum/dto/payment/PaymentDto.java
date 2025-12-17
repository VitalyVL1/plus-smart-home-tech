package ru.practicum.dto.payment;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentDto(
        UUID paymentId,
        BigDecimal totalPayment,
        BigDecimal deliveryTotal,
        BigDecimal feeTotal
) {
    @Builder
    public PaymentDto {
    }
}
