package ru.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NotNull
public class TemperatureSensorEvent extends SensorEvent {
    int temperatureC;
    int temperatureF;
    private final SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;
}
