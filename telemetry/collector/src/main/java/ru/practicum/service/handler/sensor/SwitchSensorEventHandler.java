package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.model.sensor.SwitchSensorEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {
    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent _event = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(_event.isState())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
