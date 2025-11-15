package ru.practicum.dal.model.mapper;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dal.model.*;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.dal.model.mapper.SensorMapper.mapSensor;

/**
 * Маппер для преобразования Avro-событий в доменные объекты сценариев.
 */
@Slf4j
public class ScenarioMapper {

    /**
     * Преобразует Avro-событие в доменный объект сценария.
     *
     * @param hubId        ID хаба
     * @param scenarioAvro Avro-событие сценария
     * @return доменный объект сценария или null если scenarioAvro null
     */
    public static Scenario mapScenario(String hubId, ScenarioAddedEventAvro scenarioAvro) {
        if (scenarioAvro == null) {
            return null;
        }

        Map<Sensor, Action> sensorActions = mapActions(hubId, scenarioAvro.getActions());
        Map<Sensor, Condition> sensorConditions = mapConditions(hubId, scenarioAvro.getConditions());

        return Scenario.builder()
                .hubId(hubId)
                .name(scenarioAvro.getName())
                .sensorActions(sensorActions)
                .sensorConditions(sensorConditions)
                .build();
    }

    /**
     * Преобразует список Avro-условий в мапу сенсор->условие.
     */
    private static Map<Sensor, Condition> mapConditions(String hubId, List<ScenarioConditionAvro> conditionAvros) {
        if (conditionAvros == null || conditionAvros.isEmpty()) {
            return Maps.newHashMap();
        }

        return conditionAvros.stream()
                .collect(Collectors.toMap(conditionAvro ->
                                mapSensor(hubId, conditionAvro.getSensorId()),
                        ScenarioMapper::createCondition));
    }

    /**
     * Преобразует список Avro-действий в мапу сенсор->действие.
     */
    private static Map<Sensor, Action> mapActions(String hubId, List<DeviceActionAvro> actionAvros) {
        if (actionAvros == null || actionAvros.isEmpty()) {
            return Maps.newHashMap();
        }

        return actionAvros.stream()
                .collect(Collectors.toMap(actionAvro ->
                                mapSensor(hubId, actionAvro.getSensorId()),
                        ScenarioMapper::createAction));
    }

    /**
     * Создает доменное условие из Avro-объекта.
     */
    private static Condition createCondition(ScenarioConditionAvro conditionAvro) {
        if (conditionAvro == null) {
            return null;
        }

        return Condition.builder()
                .type(ConditionType.valueOf(conditionAvro.getType().name()))
                .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                .value(extractIntegerValue(conditionAvro.getValue()))
                .build();
    }

    /**
     * Создает доменное действие из Avro-объекта.
     */
    private static Action createAction(DeviceActionAvro actionAvro) {
        if (actionAvro == null) {
            return null;
        }

        return Action.builder()
                .type(ActionType.valueOf(actionAvro.getType().name()))
                .value(actionAvro.getValue())
                .build();
    }

    /**
     * Извлекает целочисленное значение из Avro-объекта.
     * Поддерживает Integer, Boolean, Long. Для остальных типов возвращает null.
     */
    private static Integer extractIntegerValue(Object avroValue) {
        if (avroValue == null) {
            return null;
        }

        try {
            return switch (avroValue) {
                case Integer i -> i;
                case Boolean b -> Boolean.TRUE.equals(b) ? 1 : 0;
                case Long l -> l.intValue();
                default -> {
                    log.warn("Unsupported value type: {}, value: {}", avroValue.getClass(), avroValue);
                    yield null;
                }
            };
        } catch (ClassCastException e) {
            log.error("Error extracting integer value from: {}", avroValue, e);
            return null;
        }
    }

}
