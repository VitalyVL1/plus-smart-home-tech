package ru.practicum.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    private int temperatureC;

    @Min(0)
    @Max(100)
    private int humidity;

    @PositiveOrZero
    private int co2Level;
    private final SensorEventType type = SensorEventType.CLIMATE_SENSOR_EVENT;
}
