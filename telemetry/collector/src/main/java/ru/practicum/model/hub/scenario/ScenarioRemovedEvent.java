package ru.practicum.model.hub.scenario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

/**
 * Событие удаления сценария автоматизации из системы умного дома.
 * Содержит название удаляемого сценария для его поиска и удаления из системы.
 *
 * @see HubEvent
 * @see HubEventType#SCENARIO_REMOVED
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Событие удаления сценария из системы. Содержит информацию о названии удаленного сценария.")
public class ScenarioRemovedEvent extends HubEvent {

    /**
     * Название удаляемого сценария.
     * Должно точно соответствовать названию существующего сценария в системе.
     */
    @Length(min = 3)
    @Schema(description = "Название удаленного сценария. Должно содержать не менее 3 символов.")
    String name;

    /**
     * Тип события, фиксированное значение SCENARIO_REMOVED для данного класса событий.
     * Используется системой для маршрутизации событий к соответствующим обработчикам.
     */
    @Schema(description = "Перечисление типов событий хаба.",
            example = "SCENARIO_REMOVED")
    HubEventType type = HubEventType.SCENARIO_REMOVED;
}
