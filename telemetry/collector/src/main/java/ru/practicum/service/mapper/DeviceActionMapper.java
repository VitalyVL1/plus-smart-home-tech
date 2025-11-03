package ru.practicum.service.mapper;

import ru.practicum.model.hub.device.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.Collections;
import java.util.List;

/**
 * Маппер DeviceAction --> DeviceActionAvro
 * Работает как с одиночными объектами, так и со списком
 */
public class DeviceActionMapper {
    /**
     * Маппит DeviceAction в DeviceActionAvro
     *
     * @param action объект DeviceAction, может быть null
     * @return DeviceActionAvro, или null в случае если в параметры передан null
     */
    public static DeviceActionAvro map(DeviceAction action) {
        if (action == null) return null;
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }

    /**
     * Маппит список DeviceAction в список DeviceActionAvro
     *
     * @param actions список для маппинга, может быть null
     * @return неизменяемый список DeviceActionAvro, никогда не возвращает null
     */
    public static List<DeviceActionAvro> map(List<DeviceAction> actions) {
        if (actions == null || actions.isEmpty()) Collections.emptyList();
        return actions.stream().map(DeviceActionMapper::map).toList();
    }

}
