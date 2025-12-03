package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.service.ShoppingStoreService;
import ru.practicum.validator.ValidPageable;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProducts(
            @RequestParam ProductCategory category,
            @ValidPageable
            @PageableDefault(size = 20, sort = "productName", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.info("Getting products by category {}", category);
        try {
            return shoppingStoreService.getProductsByCategory(category, pageable);
        } catch (Exception e) {
            log.error("Error getting products by category {}", category, e);
            throw e;
        }
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createProduct(@RequestBody @Valid ProductDto dto) {
        log.info("Creating product with id {}", dto.productId());
        try {
            return shoppingStoreService.createProduct(dto);
        } catch (Exception e) {
            log.error("Error creating product with id {}", dto.productId(), e);
            throw e;
        }
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@RequestBody @Valid ProductDto dto) {
        log.info("Updating product with id {}", dto.productId());
        try {
            return shoppingStoreService.updateProduct(dto);
        } catch (Exception e) {
            log.error("Error updating product with id {}", dto.productId(), e);
            throw e;
        }
    }

    @Override
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProduct(@RequestBody UUID productId) {
        log.info("Removing product with id {}", productId);
        try {
            return shoppingStoreService.removeProduct(productId);
        } catch (Exception e) {
            log.error("Error removing product with id {}", productId, e);
            throw e;
        }
    }

    @Override
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public Boolean setQuantityState(@Valid @ModelAttribute SetProductQuantityStateRequest request) {
        log.info("Setting quantity state for product with id {}", request.productId());
        try {
            return shoppingStoreService.setQuantityState(request);
        } catch (Exception e) {
            log.error("Error setting quantity state for product with id {}", request.productId(), e);
            throw e;
        }
    }

    @Override
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        log.info("Getting product with id {}", productId);
        try {
            return shoppingStoreService.getProductById(productId);
        } catch (Exception e) {
            log.error("Error getting product with id {}", productId, e);
            throw e;
        }
    }
}
