package ru.practicum.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.practicum.dto.warehouse.AddressDto;

import java.util.UUID;

public record DeliveryDto(
        UUID deliveryId,
        @NotNull
        AddressDto fromAddress,
        @NotNull
        AddressDto toAddress,
        @NotNull
        UUID orderId,
        @NotNull
        DeliveryState deliveryState) {
    @Builder
    public DeliveryDto {
    }

}
