package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.model.sensor.TemperatureSensorEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent _event = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                // .setId(_event.getId())
                // .setHubId(_event.getHubId())
                .setTemperatureC(_event.getTemperatureC())
                .setTemperatureF(_event.getTemperatureF())
                .setTimestamp(_event.getTimestamp())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
