package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.BookedProduct;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с забронированными товарами.
 */
public interface BookedProductRepository extends JpaRepository<BookedProduct, UUID> {
    List<BookedProduct> findAllByOrderId(UUID orderId);

    @Modifying
    @Query("UPDATE BookedProduct bp SET bp.quantity = :newQuantity WHERE bp.orderId = :orderId")
    int updateQuantity(@Param("orderId") UUID orderId, @Param("newQuantity") Long newQuantity);
}