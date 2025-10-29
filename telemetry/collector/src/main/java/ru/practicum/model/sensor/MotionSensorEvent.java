package ru.practicum.model.sensor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    @Min(0)
    @Max(255)
    private int linkQuality;
    private boolean motion;
    private int voltage;
    private final SensorEventType type = SensorEventType.MOTION_SENSOR_EVENT;
}
