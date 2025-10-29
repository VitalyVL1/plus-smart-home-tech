package ru.practicum.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    int temperatureC;
    int temperatureF;
    private final SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;
}
