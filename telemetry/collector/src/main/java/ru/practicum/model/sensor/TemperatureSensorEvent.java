package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Событие датчика температуры, содержащее информацию о температуре в градусах Цельсия и Фаренгейта.
 * Используется для мониторинга температуры в различных помещениях и системах.
 *
 * @see SensorEvent
 * @see SensorEventType#TEMPERATURE_SENSOR_EVENT
 */
@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие датчика температуры, " +
                      "содержащее информацию о температуре в градусах Цельсия и Фаренгейта.")
public class TemperatureSensorEvent extends SensorEvent {

    /**
     * Температура в градусах Цельсия.
     */
    @Schema(description = "Температура в градусах Цельсия.")
    int temperatureC;

    /**
     * Температура в градусах Фаренгейта.
     */
    @Schema(description = "Температура в градусах Фаренгейта.")
    int temperatureF;

    /**
     * Тип события, фиксированное значение TEMPERATURE_SENSOR_EVENT.
     * Определяет обработчик и схему данных для этого типа событий.
     */
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "TEMPERATURE_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;
}
