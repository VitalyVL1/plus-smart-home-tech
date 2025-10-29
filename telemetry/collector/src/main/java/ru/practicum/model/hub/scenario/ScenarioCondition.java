package ru.practicum.model.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ScenarioCondition {
    @NotBlank
    String sensorId;

    @NotNull
    ScenarioType type;

    @NotNull
    ScenarioOperation operation;

    Integer value;
}
