package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.practicum.repository.InMemoryRepository;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.TopicType;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
public class AggregationStarter implements CommandLineRunner {

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final InMemoryRepository repository;
    private final KafkaConfig kafkaConfig;
    private static final Duration POLL_DURATION = Duration.ofSeconds(3);

    public AggregationStarter(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
        this.producer = new KafkaProducer<>(kafkaConfig.getProducerProperties());
        this.consumer = new KafkaConsumer<>(kafkaConfig.getConsumerProperties());
        this.repository = new InMemoryRepository();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting aggregation service via CommandLineRunner");
        start();
    }

    /**
     * Метод для начала процесса агрегации данных.
     * Формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        log.info("Starting aggregation service with empty initial state");
        String topic = kafkaConfig.getTopics().get(TopicType.TELEMETRY_SENSORS);

        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(POLL_DURATION);

                if (records.count() > 0) {
                    log.debug("Processing {} events", records.count());
                }

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    updateState(record.value()).ifPresent(this::sendSnapshot);
                }

                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
            log.info("Wakeup interrupted");
        } catch (Exception e) {
            log.error("Exception while trying to handle sensor event data", e);
        } finally {

            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                if (producer != null) {
                    producer.flush();
                }
                if (consumer != null) {
                    consumer.commitSync();
                }
            } finally {
                log.info("Closing consumer");
                if (consumer != null) {
                    consumer.close();
                }
                log.info("Closing producer");
                if (producer != null) {
                    producer.close();
                }
            }
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        if (event == null) {
            return Optional.empty();
        }

        log.info("Updating state for event {}", event);

        // Получаем снапшот для hubId
        Optional<SensorsSnapshotAvro> optSnapshot = repository.get(event.getHubId());
        Map<String, SensorStateAvro> states;
        SensorsSnapshotAvro snapshotAvro;

        if (optSnapshot.isPresent()) {
            snapshotAvro = optSnapshot.get();
            // Проверяем, есть ли в снапшоте данные для event.getId()
            states = new HashMap<>(snapshotAvro.getSensorsState());
            SensorStateAvro oldState = states.get(event.getId());

            if (oldState != null) {
                log.debug("Found old state {}", oldState);
                // Проверяем, нужно ли обновлять данные
                if (oldState.getTimestamp().isAfter(event.getTimestamp()) ||
                    oldState.getData().equals(event.getPayload())) {
                    return Optional.empty();
                }
            }
        } else {
            states = new HashMap<>();
        }

        // Создаём экземпляр SensorStateAvro на основе данных события
        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setData(event.getPayload())
                .setTimestamp(event.getTimestamp())
                .build();

        log.debug("Updating state by new state {}", newState);

        // Добавляем полученный экземпляр в снапшот
        states.put(event.getId(), newState);

        // Обновляем снапшот
        snapshotAvro = SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setSensorsState(states)
                .setTimestamp(event.getTimestamp())
                .build();

        log.debug("Updating snapshot {}", snapshotAvro);

        return Optional.of(repository.put(snapshotAvro));
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        String topicName = kafkaConfig.getTopics().get(TopicType.TELEMETRY_SNAPSHOTS);

        ProducerRecord<String, SensorsSnapshotAvro> record =
                new ProducerRecord<>(topicName, snapshot.getHubId(), snapshot);

        log.info("Sending snapshot {} to topic {}", snapshot, topicName);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send snapshot: {} to topic: {}, Key: {}",
                        snapshot, topicName, snapshot.getHubId(), exception);
            } else {
                log.debug("Snapshot: {} successfully sent to topic: {}, Key: {}, Partition: {}, Offset: {}",
                        snapshot, topicName, snapshot.getHubId(), metadata.partition(), metadata.offset());
            }
        });
    }
}
