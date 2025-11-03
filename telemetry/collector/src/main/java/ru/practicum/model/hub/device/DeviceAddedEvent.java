package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

/**
 * Событие, сигнализирующее о добавлении нового устройства в систему умного дома.
 * Генерируется хабом при успешном подключении и регистрации нового устройства.
 * Содержит идентификатор устройства и его тип для последующей обработки системой.
 *
 * @see HubEvent
 * @see DeviceType
 * @see HubEventType#DEVICE_ADDED
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие, сигнализирующее о добавлении нового устройства в систему.")
public class DeviceAddedEvent extends HubEvent {

    /**
     * Уникальный идентификатор добавленного устройства в системе.
     * Используется для однозначной идентификации устройства во всех последующих операциях.
     */
    @NotBlank
    @Schema(description = "Идентификатор добавленного устройства.")
    private String id;

    /**
     * Тип добавленного устройства, определяющий его функциональность и обрабатываемые события.
     * Влияет на выбор соответствующего обработчика событий для данного устройства.
     */
    @NotNull
    @Schema(description = "Перечисление типов устройств, которые могут быть добавлены в систему.",
            example = "MOTION_SENSOR, TEMPERATURE_SENSOR, LIGHT_SENSOR, CLIMATE_SENSOR, SWITCH_SENSOR")
    private DeviceType deviceType;

    /**
     * Тип события, фиксированное значение DEVICE_ADDED для данного класса событий.
     * Используется системой для маршрутизации событий к соответствующим обработчикам.
     */
    @Schema(description = "Перечисление типов событий хаба.",
            example = "DEVICE_ADDED")
    private final HubEventType type = HubEventType.DEVICE_ADDED;
}
