package com.example.back.es;

import com.example.back.utils.SerializeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventBus implements EventBus {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${microservice.kafka.topics.card-event-store}")
    private String cardTopicName;

    @Override
    public void publish(List<Event> eventList) {
        byte[] jsonBytes = SerializeUtils.serializeToJsonBytes(eventList);

        kafkaTemplate.send(cardTopicName, jsonBytes);
        log.info("publishing kafka record, topic: {}, data: {}", cardTopicName, Arrays.toString(jsonBytes));
    }
}
