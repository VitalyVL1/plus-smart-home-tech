package ru.practicum.model.hub.scenario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.hub.device.DeviceAction;

import java.util.List;

/**
 * Событие добавления нового сценария автоматизации в систему умного дома.
 * Сценарий определяет набор условий и действий для автоматизации работы устройств.
 * Содержит название сценария, список условий активации и список выполняемых действий.
 *
 * @see HubEvent
 * @see ScenarioCondition
 * @see DeviceAction
 * @see HubEventType#SCENARIO_ADDED
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие добавления сценария в систему. " +
                      "Содержит информацию о названии сценария, условиях и действиях.")
public class ScenarioAddedEvent extends HubEvent {

    /**
     * Уникальное название сценария, используемое для его идентификации в системе.
     * Должно быть читаемым и описывающим назначение сценария, длиной не менее 3-х символов.
     */
    @Length(min = 3)
    @Schema(description = "Название добавленного сценария. " +
                          "Должно содержать не менее 3 символов.")
    String name;

    /**
     * Список условий, при выполнении которых активируется сценарий.
     * Условия объединяются по логическому И (все условия должны быть выполнены).
     * Не может быть пустым - сценарий должен иметь хотя бы одно условие активации.
     */
    @NotEmpty
    @Schema(description = "Список условий, которые связаны со сценарием. Не может быть пустым.")
    List<ScenarioCondition> conditions;

    /**
     * Список действий, которые выполняются при активации сценария.
     * Действия выполняются последовательно в порядке их перечисления.
     * Не может быть пустым - сценарий должен выполнять хотя бы одно действие.
     */
    @NotEmpty
    @Schema(description = "Список действий, которые должны быть выполнены в рамках сценария. Не может быть пустым.")
    List<DeviceAction> actions;

    /**
     * Тип события, фиксированное значение SCENARIO_ADDED для данного класса событий.
     * Используется системой для маршрутизации событий к соответствующим обработчикам.
     */
    @Schema(description = "Перечисление типов событий хаба.",
            example = "SCENARIO_ADDED")
    HubEventType type = HubEventType.SCENARIO_ADDED;
}
