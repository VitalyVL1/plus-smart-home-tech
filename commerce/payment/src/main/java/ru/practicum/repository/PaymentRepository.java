package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Payment;

import java.util.UUID;

//TODO
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
