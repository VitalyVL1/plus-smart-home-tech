package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.TopicType;
import ru.practicum.dal.service.ScenarioService;
import ru.practicum.dal.service.SensorService;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> hubConsumer;
    private final KafkaConfig config;
    private final SensorService sensorService;
    private final ScenarioService scenarioService;
    private static final Duration POLL_DURATION = Duration.ofSeconds(3);

    public HubEventProcessor(KafkaConfig config, SensorService sensorService, ScenarioService scenarioService) {
        this.hubConsumer = new KafkaConsumer<>(config.getHubConsumerProperties());
        this.config = config;
        this.sensorService = sensorService;
        this.scenarioService = scenarioService;
    }

    @Override
    public void run() {
        log.info("HubEventProcessor started");
        String topic = config.getTopics().get(TopicType.TELEMETRY_HUBS);

        try (hubConsumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(hubConsumer::wakeup));
            hubConsumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubConsumer.poll(POLL_DURATION);

                if (records.count() > 0) {
                    log.debug("Processing {} records", records.count());
                }

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    hubAction(record);
                }
                hubConsumer.commitSync();
            }
        } catch (
                WakeupException ignored) {
            log.info("Wakeup interrupted");
        } catch (Exception e) {
            log.error("Exception while trying to handle hub event", e);
        } finally {
            try {
                if (hubConsumer != null) {
                    hubConsumer.commitSync();
                }
            } finally {
                log.info("Closing consumer");
                if (hubConsumer != null) {
                    hubConsumer.close();
                }
            }
        }
    }

    private void hubAction(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro hubEventAvro = record.value();
        String hubId = hubEventAvro.getHubId();

        try {
            switch (hubEventAvro.getPayload()) {
                case DeviceAddedEventAvro deviceAdd -> sensorService.save(hubId, deviceAdd);

                case DeviceRemovedEventAvro deviceRemove ->
                        sensorService.deleteBySensorIdAndHubId(deviceRemove.getId(), hubId);

                case ScenarioAddedEventAvro scenarioAdd -> scenarioService.saveOrUpdate(hubId, scenarioAdd);

                case ScenarioRemovedEventAvro scenarioRemove ->
                        scenarioService.deleteByHubIdAndName(hubId, scenarioRemove.getName());

                default -> log.warn("Unknown event type: {}", hubEventAvro.getPayload().getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("Error processing hub event for hub: {}, offset: {}",
                    hubId, record.offset(), e);
        }
    }
}
