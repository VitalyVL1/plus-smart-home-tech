package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Delivery;

import java.util.UUID;

//TODO
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
