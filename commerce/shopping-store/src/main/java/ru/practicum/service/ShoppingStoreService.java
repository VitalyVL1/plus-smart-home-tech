package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;

import java.util.UUID;

public interface ShoppingStoreService {
    ProductDto createProduct(ProductDto product);

    ProductDto updateProduct(ProductDto product);

    Boolean removeProduct(UUID productId);

    Boolean setQuantityState(SetProductQuantityStateRequest request);

    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto getProductById(UUID productId);
}
