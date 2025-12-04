package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.exception.ServiceTemporaryUnavailableException;

import java.util.UUID;

@Component
@Slf4j
public class ShoppingStoreFeignClientFallbackFactory implements FallbackFactory<ShoppingStoreFeignClient> {

    @Override
    public ShoppingStoreFeignClient create(Throwable cause) {
        return new ShoppingStoreFeignClient() {
            @Override
            public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
                log.error("Failed to get products", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public ProductDto createProduct(ProductDto dto) {
                log.error("Failed to create product", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public ProductDto updateProduct(ProductDto dto) {
                log.error("Failed to update product", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public Boolean removeProduct(UUID productId) {
                log.error("Failed to remove product", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public Boolean setQuantityState(SetProductQuantityStateRequest request) {
                log.error("Failed to set quantity state", cause);
                throw new ServiceTemporaryUnavailableException("Service is temporarily unavailable");
            }

            @Override
            public ProductDto getProduct(UUID productId) {
                log.error("Failed to get product", cause);
                return null;
            }
        };

    }
}

