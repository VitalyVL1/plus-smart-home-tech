package ru.practicum.circuit_braker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.exception.ServiceTemporaryUnavailableException;
import ru.practicum.feign.client.ShoppingStoreClient;

import java.util.UUID;

public class ShoppingStoreClientFallBack implements ShoppingStoreClient {
    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }

    @Override
    public ProductDto createProduct(ProductDto dto) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest request) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }

    @Override
    public ProductDto getProduct(String productId) {
        throw new ServiceTemporaryUnavailableException("Shopping store service is not available");
    }
}
