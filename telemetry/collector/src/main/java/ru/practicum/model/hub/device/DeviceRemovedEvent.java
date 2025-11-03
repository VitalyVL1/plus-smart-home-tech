package ru.practicum.model.hub.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

/**
 * Событие, сигнализирующее об удалении устройства из системы умного дома.
 * Генерируется хабом при отключении или deregistration устройства.
 * Содержит идентификатор удаляемого устройства для очистки связанных данных.
 *
 * @see HubEvent
 * @see HubEventType#DEVICE_REMOVED
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие, сигнализирующее об удалении устройства из системы.")
public class DeviceRemovedEvent extends HubEvent {

    /**
     * Уникальный идентификатор удаляемого устройства.
     * Используется для поиска и удаления всех связанных с устройством данных и подписок.
     */
    @NotBlank
    @Schema(description = "Идентификатор удаленного устройства.")
    private String id;

    /**
     * Тип события, фиксированное значение DEVICE_REMOVED для данного класса событий.
     * Используется системой для маршрутизации событий к соответствующим обработчикам.
     */
    @Schema(description = "Перечисление типов событий хаба.",
            example = "DEVICE_REMOVED")
    private final HubEventType type = HubEventType.DEVICE_REMOVED;
}
