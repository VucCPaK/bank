package com.example.back.projections;

import com.example.back.domain.CardAggregate;
import com.example.back.domain.CardDocument;
import com.example.back.es.Event;
import com.example.back.es.EventStore;
import com.example.back.es.Projection;
import com.example.back.events.CreateCardEvent;
import com.example.back.events.DepositAmountEvent;
import com.example.back.events.EventType;
import com.example.back.events.WithdrawAmountEvent;
import com.example.back.exceptions.CardNotFoundException;
import com.example.back.exceptions.InsufficientFundsException;
import com.example.back.exceptions.InvalidEventTypeException;
import com.example.back.mappers.CardMapper;
import com.example.back.repositories.CardRepository;
import com.example.back.utils.SerializeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CardProjection implements Projection {

    private final EventStore eventStore;
    private final CardRepository cardRepository;


    @KafkaListener(topics = "${microservice.kafka.topics.card-event-store}",
            groupId = "${microservice.kafka.group-id}",
            concurrency = "${microservice.kafka.default-concurrency}")
    public void cardProjectionListener(@Payload byte[] data, ConsumerRecordMetadata metadata) {
        log.info("(cardProjectionListener) topic: {}, offset: {}, partition: {}, data: {}",
                metadata.topic(), metadata.offset(), metadata.partition(), Arrays.toString(data));

        Event[] events = SerializeUtils.deserializeFromJsonBytes(data, Event[].class);
        this.processEvents(Arrays.stream(events).toList());

    }

    private void processEvents(List<Event> events) {
        if (events.isEmpty()) return;

        try {
            events.forEach(this::when);
        } catch (Exception e) {
            cardRepository.deleteByAggregateId(events.get(0).getAggregateId());
            var aggregate = eventStore.load(events.get(0).getAggregateId(), CardAggregate.class);
            var document = CardMapper.cardDocumentFromCardAggregate(aggregate);
            var savedDocument = cardRepository.save(document);
            log.info("(processEvents) saved document: {}", savedDocument);
        }
    }

    @Override
    public void when(Event event) {
        switch (EventType.valueOf(event.getEventType())) {
            case CREATE_CARD ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), CreateCardEvent.class));
            case DEPOSIT_AMOUNT ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), DepositAmountEvent.class));
            case WITHDRAW_AMOUNT ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), WithdrawAmountEvent.class));
            default -> throw new InvalidEventTypeException(event.toString());
        }
    }

    private void handle(CreateCardEvent event) {
        var document = CardDocument.builder()
                .aggregateId(event.getAggregateId())
                .CVC(event.getCVC())
                .currency(event.getCurrency())
                .expirationDate(event.getExpirationDate())
                .balance(BigDecimal.valueOf(0))
                .build();

        var insert = cardRepository.insert(document);
        log.info("(CreateCardEvent) insert: {}", insert);
    }

    private void handle(DepositAmountEvent event) {
        var documentOptional = cardRepository.findCardDocumentByAggregateId(event.getAggregateId());

        if (documentOptional.isEmpty())
            throw new CardNotFoundException(event.getAggregateId());

        var document = documentOptional.get();
        var newBalance = document.getBalance().add(event.getDepositAmount());
        document.setBalance(newBalance);
        cardRepository.save(document);
        log.info("(DepositAmountEvent) balance deposited on document: {}", document);
    }

    private void handle(WithdrawAmountEvent event) {
        var documentOptional = cardRepository.findCardDocumentByAggregateId(event.getAggregateId());

        if (documentOptional.isEmpty())
            throw new CardNotFoundException(event.getAggregateId());

        var document = documentOptional.get();

        if (event.getWithdrawAmount().compareTo(document.getBalance()) > 0)
            throw new InsufficientFundsException(document.getBalance(), event.getWithdrawAmount());

        var newBalance = document.getBalance().subtract(event.getWithdrawAmount());
        document.setBalance(newBalance);
        cardRepository.save(document);
        log.info("(DepositAmountEvent) balance withdrawn on document: {}", document);
    }
}
