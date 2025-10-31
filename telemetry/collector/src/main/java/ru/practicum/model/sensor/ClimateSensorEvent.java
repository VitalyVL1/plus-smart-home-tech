package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Событие климатического датчика, содержащее комплексные данные о микроклимате помещения.
 * Включает показатели температуры, влажности и уровня углекислого газа (CO2).
 * Используется для мониторинга и управления системами климат-контроля.
 *
 * @see SensorEvent
 * @see SensorEventType#CLIMATE_SENSOR_EVENT
 */
@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие климатического датчика, содержащее информацию о температуре, влажности и уровне CO2.")
public class ClimateSensorEvent extends SensorEvent {

    /**
     * Температура окружающей среды в градусах Цельсия.
     */
    @Schema(description = "Уровень температуры по шкале Цельсия.")
    private int temperatureC;

    /**
     * Относительная влажность воздуха в процентах.
     */
    @Schema(description = "Влажность.")
    private int humidity;

    /**
     * Концентрация углекислого газа в воздухе в ppm (parts per million).
     */
    @Schema(description = "Уровень CO2.")
    private int co2Level;

    /**
     * Тип события, фиксированное значение CLIMATE_SENSOR_EVENT.
     * Определяет обработчик и схему данных для этого типа событий.
     */
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "CLIMATE_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.CLIMATE_SENSOR_EVENT;
}
