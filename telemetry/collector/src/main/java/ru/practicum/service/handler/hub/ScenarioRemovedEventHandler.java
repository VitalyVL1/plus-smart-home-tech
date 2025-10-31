package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.hub.scenario.ScenarioRemovedEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

/**
 * Обработчик событий удаления сценариев из хаба.
 * Преобразует ScenarioRemovedEvent в Avro-формат и отправляет в Kafka топик TELEMETRY_HUBS.
 * Событие удаления сценария содержит информацию об имени удаляемого сценария.
 *
 * @see BaseHubEventHandler
 * @see ScenarioRemovedEvent
 * @see ScenarioRemovedEventAvro
 */
@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    /**
     * Конструктор обработчика событий удаления сценариев.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEvent в ScenarioRemovedEventAvro.
     * Создает Avro-представление события удаления сценария на основе имени сценария.
     *
     * @param event событие удаления сценария, должно быть типа ScenarioRemovedEvent
     * @return Avro-представление события удаления сценария
     * @throws ClassCastException если event не является ScenarioRemovedEvent
     */
    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent _event = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(_event.getName())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события SCENARIO_REMOVED
     */
    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
