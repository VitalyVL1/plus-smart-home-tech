package ru.practicum.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.GeneralAvroSerializer;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public Properties kafkaProducerConfig() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        // батчинг
        config.put(ProducerConfig.LINGER_MS_CONFIG, 20); // Задержка для батчинга
        // config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // Размер батча
        // config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // Размер буфера

        //доставка
        config.put(ProducerConfig.ACKS_CONFIG, "all"); // Гарантия доставки
        config.put(ProducerConfig.RETRIES_CONFIG, 3); // Количество повторов
        // config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);  // Идемпотентность

        // config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // Количество запросов на соединение
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // таймаут одного запроса к брокеру
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000); // общий таймаут со всеми ретраями

        return config;
    }
}
