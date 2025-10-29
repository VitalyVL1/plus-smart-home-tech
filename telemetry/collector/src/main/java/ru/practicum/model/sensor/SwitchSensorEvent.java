package ru.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NotNull
public class SwitchSensorEvent extends SensorEvent {
    private boolean state;
    private final SensorEventType type = SensorEventType.SWITCH_SENSOR_EVENT;
}
