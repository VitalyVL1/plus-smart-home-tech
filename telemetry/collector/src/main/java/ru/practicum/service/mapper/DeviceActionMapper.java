package ru.practicum.service.mapper;

import ru.practicum.model.hub.device.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.List;

public class DeviceActionMapper {
    public static DeviceActionAvro map(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(EnumMapper.map(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }

    public static List<DeviceActionAvro> map(List<DeviceAction> action) {
        return action.stream().map(DeviceActionMapper::map).toList();
    }

}
