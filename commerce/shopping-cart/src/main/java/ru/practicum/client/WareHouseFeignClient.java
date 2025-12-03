package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WareHouseFeignClient extends WarehouseClient {

}
