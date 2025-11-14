package ru.practicum.dal.model.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.dal.model.Action;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;

import java.util.Arrays;

@Slf4j
public class ActionMapper {
    public static DeviceActionProto toDeviceActionProto(String sensorId, Action action) {
        if (action == null || sensorId == null) {
            return null;
        }

        try {
            DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(
                            ActionTypeProto.valueOf(action.getType().toString())
                    )
                    .setValue(action.getValue())
                    .build();
            return deviceActionProto;
        } catch (IllegalArgumentException e) {
            log.error("Unknown action type: {}. Available types: {}. Action: {}",
                    action.getType(),
                    Arrays.toString(ActionTypeProto.values()),
                    action);
            return null;
        }
    }
}
