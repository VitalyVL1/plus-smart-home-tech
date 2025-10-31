package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Событие датчика-переключателя, содержащее информацию о текущем состоянии переключателя.
 * Используется для управления устройствами и отслеживания их состояния.
 *
 * @see SensorEvent
 * @see SensorEventType#SWITCH_SENSOR_EVENT
 */
@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие датчика переключателя, содержащее информацию о текущем состоянии переключателя.")
public class SwitchSensorEvent extends SensorEvent {

    /**
     * Текущее состояние переключателя.
     * true - включен/активирован, false - выключен/деактивирован.
     * Используется для управления устройствами и отслеживания их состояния.
     */
    @Schema(description = "Текущее состояние переключателя. true - включен, false - выключен.")
    private boolean state;

    /**
     * Тип события, фиксированное значение SWITCH_SENSOR_EVENT.
     * Определяет обработчик и схему данных для этого типа событий.
     */
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "SWITCH_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.SWITCH_SENSOR_EVENT;
}
