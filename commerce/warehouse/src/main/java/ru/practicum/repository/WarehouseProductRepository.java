package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.WarehouseProduct;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID> {
    List<WarehouseProduct> findAllByProductIdIn(Set<UUID> productIdList);

    //в случае работы с несколькими складами, такой метод будет выдавать список на разных складах
    Optional<WarehouseProduct> findByProductId(UUID productId);
}
