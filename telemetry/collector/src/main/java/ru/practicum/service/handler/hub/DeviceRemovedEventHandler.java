package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.hub.device.DeviceRemovedEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

/**
 * Обработчик событий удаления устройств из хаба.
 * Преобразует DeviceRemovedEvent в Avro-формат и отправляет в Kafka топик TELEMETRY_HUBS.
 *
 * @see BaseHubEventHandler
 * @see DeviceRemovedEvent
 * @see DeviceRemovedEventAvro
 */
@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    /**
     * Конструктор обработчика событий добавления устройств.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEvent в DeviceRemovedEventAvro.
     *
     * @param event событие удаления устройства, должно быть типа DeviceRemovedEvent
     * @return Avro-представление события удаления устройства
     * @throws ClassCastException если event не является DeviceRemovedEvent
     */
    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent _event = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(_event.getId())
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события DEVICE_REMOVED
     */
    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
