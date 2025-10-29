package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.TopicType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class KafkaEventProducer implements AutoCloseable {
    protected final KafkaProducer<String, SpecificRecordBase> producer;
    protected final EnumMap<TopicType, String> topics;

    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        topics = kafkaConfig.kafkaTopics();

        Properties properties = kafkaConfig.kafkaProducerProperties();
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 20); // Задержка для батчинга
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); // Гарантия доставки
        properties.put(ProducerConfig.RETRIES_CONFIG, 3); // Количество повторов
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // таймаут одного запроса к брокеру
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000); // общий таймаут со всеми ретраями

        producer = new KafkaProducer<>(kafkaConfig.kafkaProducerProperties());
    }

    /**
     * Отправка события в Kafka
     */
    public <T extends SpecificRecordBase> void sendEvent(TopicType topicType, String key, T event) {
        String topicName = getTopicName(topicType);

        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(topicName, key, event);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send event: {} to topic: {}, Key: {}",
                        event, topicName, key, exception);
            } else {
                log.debug("Event: {} successfully sent to topic: {}, Key: {}, Partition: {}, Offset: {}",
                        event, topicName, key, metadata.partition(), metadata.offset());
            }
        });
    }

    /**
     * Получение имени топика по типу
     */
    public String getTopicName(TopicType topicType) {
        String topicName = topics.get(topicType);
        if (topicName == null) {
            throw new IllegalArgumentException("Unknown topic type: " + topicType);
        }
        return topicName;
    }

    /**
     * Получение всех топиков (для инициализации)
     */
    public List<String> getAllTopicNames() {
        return new ArrayList<>(topics.values());
    }

    public void flush() {
        producer.flush();
    }

    @Override
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

}
