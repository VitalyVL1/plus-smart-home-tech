package ru.practicum.feign.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.validator.ValidPageable;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductDto> getProducts(
            @RequestParam ProductCategory category,
            @ValidPageable
            @PageableDefault(size = 20, sort = "productName", direction = Sort.Direction.ASC)
            Pageable pageable);

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto dto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto dto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    Boolean setQuantityState(@Valid @ModelAttribute SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable String productId);
}
