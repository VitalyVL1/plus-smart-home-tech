package ru.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import ru.practicum.config.TopicType;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecord> implements SensorEventHandler {

    private final KafkaEventProducer producer;
    private static final TopicType TOPIC_TYPE = TopicType.TELEMETRY_SENSORS;

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        T avroEvent = mapToAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(avroEvent)
                .build();

        try {
            producer.sendEvent(TOPIC_TYPE, event.getHubId(), sensorEventAvro);
        } catch (Exception e) {
            log.error("Error processing SensorEvent. SensorEventAvro: {}", sensorEventAvro, e);
            throw new RuntimeException("Failed to process SensorEvent", e);
        }
    }

    // Метод для закрытия producer
    public void shutdown() {
        if (producer != null) {
            producer.close();
        }
    }
}
