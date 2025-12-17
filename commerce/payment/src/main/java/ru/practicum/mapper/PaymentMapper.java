package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.model.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}
