package ru.practicum.service.mapper;

import ru.practicum.model.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.Collections;
import java.util.List;

/**
 * Маппер ScenarioCondition --> ScenarioConditionAvro
 * Работает как с одиночными объектами, так и со списком
 */
public class ScenarioConditionMapper {
    /**
     * Маппит ScenarioCondition в ScenarioConditionAvro
     *
     * @param condition объект ScenarioCondition, может быть null
     * @return ScenarioConditionAvro, или null в случае если в параметры передан null
     */
    public static ScenarioConditionAvro map(ScenarioCondition condition) {
        if (condition == null) return null;
        return ScenarioConditionAvro.newBuilder()
                .setOperation(EnumMapper.map(condition.getOperation(), ConditionOperationAvro.class))
                .setSensorId(condition.getSensorId())
                .setType(EnumMapper.map(condition.getType(), ConditionTypeAvro.class))
                .setValue(condition.getValue())
                .build();
    }

    /**
     * Маппит список ScenarioCondition в список ScenarioConditionAvro
     *
     * @param conditions список для маппинга, может быть null
     * @return неизменяемый список ScenarioConditionAvro, никогда не возвращает null
     */
    public static List<ScenarioConditionAvro> map(List<ScenarioCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) Collections.emptyList();
        return conditions.stream().map(ScenarioConditionMapper::map).toList();
    }
}
