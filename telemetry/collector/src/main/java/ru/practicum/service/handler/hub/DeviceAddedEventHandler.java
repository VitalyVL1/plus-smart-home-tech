package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;
import ru.practicum.model.hub.device.DeviceAddedEvent;
import ru.practicum.service.KafkaEventProducer;
import ru.practicum.service.mapper.EnumMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

/**
 * Обработчик событий добавления новых устройств в хаб.
 * Преобразует DeviceAddedEvent в DeviceAddedEventAvro и отправляет в Kafka топик TELEMETRY_HUBS.
 *
 * @see BaseHubEventHandler
 * @see DeviceAddedEvent
 * @see DeviceAddedEventAvro
 */
@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    /**
     * Конструктор обработчика событий добавления устройств.
     *
     * @param producer Kafka продюсер для отправки событий
     */
    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    /**
     * Преобразует HubEvent в DeviceAddedEventAvro.
     *
     * @param event событие добавления устройства, должно быть типа DeviceAddedEvent
     * @return DeviceAddedEventAvro Avro-представление события добавления устройства
     * @throws ClassCastException если event не является DeviceAddedEvent
     */
    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent _event = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(_event.getId())
                .setType(EnumMapper.map(_event.getDeviceType(), DeviceTypeAvro.class))
                .build();
    }

    /**
     * Возвращает тип обрабатываемого события.
     *
     * @return тип события DEVICE_ADDED
     */
    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }
}
