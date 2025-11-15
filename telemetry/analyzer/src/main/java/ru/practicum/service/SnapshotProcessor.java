package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.TopicType;
import ru.practicum.dal.model.Condition;
import ru.practicum.dal.model.ConditionType;
import ru.practicum.dal.model.Scenario;
import ru.practicum.dal.service.ScenarioService;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

/**
 * Обработчик снимков состояния сенсоров.
 * Анализирует текущее состояние сенсоров и запускает сценарии при выполнении условий.
 */
@Component
@Slf4j
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final KafkaConfig config;

    private final ScenarioService scenarioService;
    private final HubRouterProcessor hubRouterProcessor;

    private static final Duration POLL_DURATION = Duration.ofSeconds(3);

    public SnapshotProcessor(KafkaConfig config, ScenarioService scenarioService, HubRouterProcessor hubRouterProcessor) {
        this.config = config;
        this.snapshotConsumer = new KafkaConsumer<>(config.getSnapshotConsumerProperties());
        this.scenarioService = scenarioService;
        this.hubRouterProcessor = hubRouterProcessor;
    }

    /**
     * Запускает обработку снимков состояния сенсоров.
     * Читает данные из Kafka и проверяет выполнение условий сценариев.
     */
    public void start() {
        log.info("Starting SnapshotProcessor");
        String topic = config.getTopics().get(TopicType.TELEMETRY_SNAPSHOTS);

        try (snapshotConsumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));
            snapshotConsumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(POLL_DURATION);

                if (records.count() > 0) {
                    log.debug("Processing {} snapshots", records.count());
                }

                records.forEach(this::handleSnapshot);
                snapshotConsumer.commitSync();
            }

        } catch (WakeupException ignored) {
            log.info("Wakeup interrupted");
        } catch (Exception e) {
            log.error("Exception while trying to handle snapshot", e);
        } finally {
            try {
                if (snapshotConsumer != null) {
                    snapshotConsumer.commitSync();
                }
            } finally {
                log.info("Closing consumer");
                if (snapshotConsumer != null) {
                    snapshotConsumer.close();
                }
            }
        }
    }

    /**
     * Обрабатывает один снимок состояния сенсоров.
     * Проверяет все сценарии хаба и выполняет те, условия которых выполнены.
     *
     * @param record запись из Kafka со снимком состояния
     */
    private void handleSnapshot(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        SensorsSnapshotAvro sensorsSnapshotAvro = record.value();

        //находим все сценарии к хабу
        List<Scenario> scenarios = scenarioService.findByHubId(sensorsSnapshotAvro.getHubId());

        scenarios.stream()
                .filter(scenario -> isAllConditionSatisfied(sensorsSnapshotAvro, scenario))  // выбираем только те сценарии которые удовлетворяют всем условиям
                .forEach(this::executeScenario);                                                     // выполняем действия данного сценария

    }

    /**
     * Проверяет выполнение условия для одного сенсора.
     *
     * @param sensorData данные сенсора
     * @param condition  условие для проверки
     * @return true если условие выполнено
     */
    private boolean isConditionSatisfied(SpecificRecordBase sensorData, Condition condition) {
        Integer sensorValue = extractSensorValue(sensorData, condition.getType());
        return sensorValue != null && compareWithOperation(sensorValue, condition);
    }

    /**
     * Проверяет выполнение всех условий сценария.
     *
     * @param sensorsSnapshotAvro снимок состояния всех сенсоров
     * @param scenario            сценарий для проверки
     * @return true если все условия сценария выполнены
     */
    private boolean isAllConditionSatisfied(SensorsSnapshotAvro sensorsSnapshotAvro, Scenario scenario) {
        return scenario.getSensorConditions().entrySet().stream()
                .allMatch(entry -> {
                    SensorStateAvro state = sensorsSnapshotAvro.getSensorsState().get(entry.getKey().getId());
                    return state != null && isConditionSatisfied((SpecificRecordBase) state.getData(), entry.getValue());
                });
    }

    /**
     * Извлекает значение сенсора в зависимости от его типа.
     *
     * @param sensorData данные сенсора
     * @param type       тип условия
     * @return числовое значение сенсора или null если тип не поддерживается
     */
    private Integer extractSensorValue(SpecificRecordBase sensorData, ConditionType type) {
        return switch (type) {
            case TEMPERATURE -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getTemperatureC();
                if (sensorData instanceof TemperatureSensorAvro temp) yield temp.getTemperatureC();
                yield null;
            }
            case HUMIDITY -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getHumidity();
                yield null;
            }
            case CO2LEVEL -> {
                if (sensorData instanceof ClimateSensorAvro climate) yield climate.getCo2Level();
                yield null;
            }
            case LUMINOSITY -> {
                if (sensorData instanceof LightSensorAvro light) yield light.getLuminosity();
                yield null;
            }
            case MOTION -> {
                if (sensorData instanceof MotionSensorAvro motion) yield motion.getMotion() ? 1 : 0;
                yield null;
            }
            case SWITCH -> {
                if (sensorData instanceof SwitchSensorAvro switchSensor) yield switchSensor.getState() ? 1 : 0;
                yield null;
            }
        };
    }

    /**
     * Сравнивает значение сенсора с условием по указанной операции.
     *
     * @param sensorValue значение сенсора
     * @param condition   условие сравнения
     * @return true если сравнение успешно
     */
    private boolean compareWithOperation(Integer sensorValue, Condition condition) {
        return switch (condition.getOperation()) {
            case EQUALS -> sensorValue.equals(condition.getValue());
            case GREATER_THAN -> sensorValue > condition.getValue();
            case LOWER_THAN -> sensorValue < condition.getValue();
        };
    }

    /**
     * Выполняет действия сценария.
     * Отправляет все действия сценария через HubRouterProcessor.
     *
     * @param scenario сценарий для выполнения
     */
    private void executeScenario(Scenario scenario) {
        if (scenario.getSensorActions() != null) {               // хоть поле обязательное для заполнения, на всякий случай оставляем проверку на Null
            scenario.getSensorActions().entrySet().stream()      // для каждого сенсора запускаем все действия из сценария
                    .forEach(entity ->
                            hubRouterProcessor.handleAction(
                                    scenario.getHubId(),
                                    entity.getKey().getId(),
                                    scenario.getName(),
                                    entity.getValue())
                    );
        }
    }
}
