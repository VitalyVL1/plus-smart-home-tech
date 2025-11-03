package ru.practicum.model.sensor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Событие датчика движения, содержащее информацию о наличии движения, качестве связи и напряжении питания.
 *
 * @see SensorEvent
 * @see SensorEventType#MOTION_SENSOR_EVENT
 */
@Getter
@Setter
@ToString(callSuper = true)
@NotNull
@Schema(description = "Событие датчика движения.")
public class MotionSensorEvent extends SensorEvent {

    /**
     * Качество беспроводной связи датчика движения.
     */
    @Schema(description = "Качество связи.")
    private int linkQuality;

    /**
     * Флаг обнаружения движения. true - движение обнаружено, false - движение отсутствует.
     * Основной параметр для активации сценариев автоматизации.
     */
    @Schema(description = "Наличие/отсутствие движения.")
    private boolean motion;

    /**
     * Напряжение питания датчика в милливольтах.
     * Используется для мониторинга состояния батареи и своевременного уведомления о низком заряде.
     */
    @Schema(description = "Напряжение.")
    private int voltage;

    /**
     * Тип события, фиксированное значение MOTION_SENSOR_EVENT.
     * Определяет обработчик и схему данных для этого типа событий.
     */
    @Schema(description = "Перечисление типов событий датчиков. " +
                          "Определяет различные типы событий, которые могут быть связаны с датчиками.",
            example = "MOTION_SENSOR_EVENT")
    private final SensorEventType type = SensorEventType.MOTION_SENSOR_EVENT;
}
