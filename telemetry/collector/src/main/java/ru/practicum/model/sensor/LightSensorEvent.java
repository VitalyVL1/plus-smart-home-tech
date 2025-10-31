package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Событие датчика освещенности, содержащее информацию об уровне освещенности и качестве беспроводной связи.
 * Используется для систем автоматического управления освещением.
 *
 * @see SensorEvent
 * @see SensorEventType#LIGHT_SENSOR_EVENT
 */
@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие датчика освещенности, содержащее информацию о качестве связи и уровне освещенности.")
public class LightSensorEvent extends SensorEvent {

    /**
     * Качество беспроводной связи датчика в условных единицах.
     * Более высокие значения указывают на лучшее качество сигнала.
     */
    @Schema(description = "Качество связи.")
    private int linkQuality;

    /**
     * Уровень освещенности в люксах.
     * Используется для автоматического включения/выключения освещения в зависимости от естественной освещенности.
     */
    @Schema(description = "Уровень освещенности.")
    private int luminosity;

    /**
     * Тип события, фиксированное значение LIGHT_SENSOR_EVENT.
     * Определяет обработчик и схему данных для этого типа событий.
     */
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "LIGHT_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;
}
