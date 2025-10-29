package ru.practicum.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {

    @Min(0)
    @Max(255)
    private int linkQuality;

    @Min(0)
    @Max(100)
    private int luminosity;
    private final SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;
}
