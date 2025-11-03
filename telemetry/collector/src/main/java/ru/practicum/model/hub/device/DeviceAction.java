package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Модель действия устройства, которое должно быть выполнено при срабатывании сценария.
 * Содержит информацию о целевом датчике, типе действия и опциональном значении.
 * Используется для описания бизнес-логики автоматизации в сценариях.
 *
 * @see DeviceActionType
 */
@Getter
@Setter
@ToString
@Schema(description = "Представляет действие, которое должно быть выполнено устройством.")
public class DeviceAction {

    /**
     * Уникальный идентификатор датчика, к которому применяется действие.
     * Должен соответствовать существующему датчику в системе.
     */
    @NotBlank
    @Schema(description = "Идентификатор датчика, связанного с действием.")
    String sensorId;

    /**
     * Тип действия, определяющий операцию, которую необходимо выполнить над датчиком.
     * Включает такие операции как активация, деактивация, инверсия состояния или установка значения.
     */
    @NotNull
    @Schema(description = "Перечисление возможных типов действий при срабатывании условия активации сценария.",
            example = "ACTIVATE, DEACTIVATE, INVERSE, SET_VALUE")
    DeviceActionType type;

    /**
     * Опциональное значение действия. Используется для действий типа SET_VALUE.
     * Например, установка конкретной температуры или уровня яркости.
     * Может быть null для действий, не требующих числового значения.
     */
    @Schema(description = "Необязательное значение, связанное с действием.")
    Integer value;
}
