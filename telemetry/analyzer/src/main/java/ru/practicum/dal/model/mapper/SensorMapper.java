package ru.practicum.dal.model.mapper;

import ru.practicum.dal.model.Sensor;

public class SensorMapper {
    public static Sensor mapSensor(String hubId, String sensorId) {
        return Sensor.builder()
                .id(sensorId)
                .hubId(hubId)
                .build();
    }
}
