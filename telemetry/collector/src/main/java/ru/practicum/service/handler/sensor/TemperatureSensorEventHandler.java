package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;
import ru.practicum.model.sensor.TemperatureSensorEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

/**
 * Обработчик событий датчиков температуры.
 * Преобразует TemperatureSensorEvent в TemperatureSensorAvro и отправляет в Kafka топик TELEMETRY_SENSORS.
 * Обрабатывает данные о температуре в градусах Цельсия и Фаренгейта с временной меткой.
 *
 * @see BaseSensorEventHandler
 * @see TemperatureSensorEvent
 * @see TemperatureSensorAvro
 * @see SensorEventType#TEMPERATURE_SENSOR_EVENT
 */
@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    /**
     * Конструктор обработчика событий датчиков температуры.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует SensorEvent в TemperatureSensorAvro.
     * Выполняет маппинг данных датчика температуры: значений в градусах Цельсия и Фаренгейта.
     *
     * @param event событие датчика температуры, должно быть типа TemperatureSensorEvent
     * @return Avro-представление данных датчика температуры
     * @throws ClassCastException       если event не является TemperatureSensorEvent
     * @throws IllegalArgumentException если температурные данные некорректны
     */
    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent _event = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(_event.getTemperatureC())
                .setTemperatureF(_event.getTemperatureF())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события датчика температуры.
     *
     * @return тип события TEMPERATURE_SENSOR_EVENT
     */
    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
