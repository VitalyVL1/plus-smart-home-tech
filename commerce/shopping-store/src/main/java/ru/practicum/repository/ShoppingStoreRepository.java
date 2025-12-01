package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.model.Product;

import java.util.UUID;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}
