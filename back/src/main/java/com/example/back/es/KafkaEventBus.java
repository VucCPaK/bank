package com.example.back.es;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaEventBus implements EventBus {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${microservice.kafka.topics.card-event-store}")
    private String cardTopicName;

    @Override
    public void publish(List<Event> eventList) {
        byte[] jsonBytes = SerializeUtils.serializeToJsonBytes(eventList);

        kafkaTemplate.send(cardTopicName, jsonBytes);
    }
}
