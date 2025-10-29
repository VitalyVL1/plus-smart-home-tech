package ru.practicum.model.hub.device_action;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedEvent {
    @NotBlank
    private String id;
    private final HubEventType type = HubEventType.DEVICE_REMOVED;
}
