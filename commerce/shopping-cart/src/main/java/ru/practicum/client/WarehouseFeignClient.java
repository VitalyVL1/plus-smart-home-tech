package ru.practicum.client;


import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.config.WarehouseFeignClientConfig;

@FeignClient(
        name = "warehouse",
        path = "/api/v1/warehouse",
        configuration = WarehouseFeignClientConfig.class,
        fallbackFactory = WarehouseFeignClientFallbackFactory.class)
public interface WarehouseFeignClient extends WarehouseClient {
}
