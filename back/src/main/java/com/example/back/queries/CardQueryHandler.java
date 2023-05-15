package com.example.back.queries;

import com.example.back.DTO.CardDTO;
import com.example.back.DTO.CardOperation;
import com.example.back.DTO.TransactionsHistory;
import com.example.back.domain.CardAggregate;
import com.example.back.domain.CardDocument;
import com.example.back.es.Event;
import com.example.back.es.EventStore;
import com.example.back.events.DepositAmountEvent;
import com.example.back.events.EventType;
import com.example.back.events.WithdrawAmountEvent;
import com.example.back.mappers.CardMapper;
import com.example.back.repositories.CardRepository;
import com.example.back.utils.SerializeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class CardQueryHandler implements CardQueryService {

    private final CardRepository cardRepository;
    private final EventStore eventStore;

    @Override
    public CardDTO handle(GetCardByAggregateId query) {
        Optional<CardDocument> optionalCardDocument = cardRepository.findCardDocumentByAggregateId(query.aggregateId());
        if (optionalCardDocument.isPresent()) {
            return CardMapper.cardDTOFromCardDocument(optionalCardDocument.get());
        }

        var aggregate = eventStore.load(query.aggregateId(), CardAggregate.class);
        var cardDocument = CardMapper.cardDocumentFromCardAggregate(aggregate);
        cardRepository.save(cardDocument);

        return CardMapper.cardDTOFromCardAggregate(aggregate);
    }

    @Override
    public CardDTO handle(GetCardByCustomerId query) {
        Optional<CardDocument> optionalCardDocument = cardRepository.findCardDocumentByCustomerId(query.customerId());
        return optionalCardDocument.map(CardMapper::cardDTOFromCardDocument).orElse(null);
    }

    @Override
    public BigDecimal handle(GetCardBalanceByAggregateId query) {
        var aggregate = eventStore.load(query.aggregateId(), CardAggregate.class);

        log.info("(calculateBalance) balance: {} of aggregate: {}", aggregate.getBalance(), aggregate.getId());
        return aggregate.getBalance();
    }

    @Override
    public TransactionsHistory handle(GetTransactionsHistory query) {
        List<Event> eventsRelativeWithDepositOrWithdrawOperation =
                eventStore.loadEventsRelativeWithDepositOrWithdrawOperation(query.aggregateId());

        List<CardOperation> cardOperations =
                this.getCardOperations(eventsRelativeWithDepositOrWithdrawOperation);

        return new TransactionsHistory(cardOperations);
    }

    private List<CardOperation> getCardOperations(List<Event> events) {
        List<CardOperation> cardOperations = new ArrayList<>();

        events.forEach(event -> {
            switch (EventType.valueOf(event.getEventType())) {
                case WITHDRAW_AMOUNT -> {
                    var withdrawAmountEvent =
                            SerializeUtils.deserializeFromJsonBytes(event.getData(), WithdrawAmountEvent.class);

                    cardOperations.add(
                            new CardOperation(
                                    EventType.WITHDRAW_AMOUNT,
                                    withdrawAmountEvent.getWithdrawAmount(),
                                    event.getTimestamp()));
                }

                case DEPOSIT_AMOUNT -> {
                    var depositAmountEvent =
                            SerializeUtils.deserializeFromJsonBytes(event.getData(), DepositAmountEvent.class);

                    cardOperations.add(
                            new CardOperation(
                                    EventType.DEPOSIT_AMOUNT,
                                    depositAmountEvent.getDepositAmount(),
                                    event.getTimestamp()));
                }
            }
        });

        return cardOperations;
    }
}
