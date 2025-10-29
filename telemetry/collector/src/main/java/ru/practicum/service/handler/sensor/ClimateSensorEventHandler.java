package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.ClimateSensorEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent _event = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(_event.getCo2Level())
                .setHumidity(_event.getHumidity())
                .setTemperatureC(_event.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
