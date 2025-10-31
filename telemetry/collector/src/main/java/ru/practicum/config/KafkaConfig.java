package ru.practicum.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.GeneralAvroSerializer;

import java.util.EnumMap;
import java.util.Properties;

/**
 * Конфигурационный класс для настройки Kafka.
 * Предоставляет бины для работы с топиками Kafka и настройками продюсера.
 * Содержит маппинг типов топиков на их фактические имена и базовую конфигурацию KafkaProducer.
 *
 * @see TopicType
 */
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    /**
     * Создает маппинг типов топиков на их фактические имена в Kafka.
     * Используется для маршрутизации событий в соответствующие топики.
     *
     * @return EnumMap, связывающая каждый тип топика с его именем
     * @see TopicType
     */
    @Bean
    public EnumMap<TopicType, String> kafkaTopics() {
        EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);
        topics.put(TopicType.TELEMETRY_SENSORS, "telemetry.sensors.v1");
        topics.put(TopicType.TELEMETRY_SNAPSHOTS, "telemetry.snapshots.v1");
        topics.put(TopicType.TELEMETRY_HUBS, "telemetry.hubs.v1");
        return topics;
    }

    /**
     * Создает базовую конфигурацию для KafkaProducer.
     * Настраивает основные параметры: серверы, сериализаторы ключей и значений.
     * Дополнительные настройки (такие как лимиты, таймауты, ретраи) могут быть добавлены
     * в конкретных реализациях продюсера.
     *
     * @return Properties с базовой конфигурацией KafkaProducer
     * @see ProducerConfig
     * @see StringSerializer
     * @see GeneralAvroSerializer
     */
    @Bean
    public Properties kafkaProducerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        return properties;
    }
}
