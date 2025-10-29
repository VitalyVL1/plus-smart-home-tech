package ru.practicum.model.hub.scenario;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {

    @Length(min = 3)
    String name;

    HubEventType type = HubEventType.SCENARIO_REMOVED;
}
