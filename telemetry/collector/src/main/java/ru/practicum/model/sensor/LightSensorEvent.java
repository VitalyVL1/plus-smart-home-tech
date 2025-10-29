package ru.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NotNull
public class LightSensorEvent extends SensorEvent {
    private int linkQuality;
    private int luminosity;
    private final SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;
}
