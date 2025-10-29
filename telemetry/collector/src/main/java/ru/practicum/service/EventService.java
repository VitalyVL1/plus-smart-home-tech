package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Service;
import ru.practicum.model.hub.HubEvent;

import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    Properties kafkaProducerConfig;
    Producer<String, HubEvent> producer;

}
