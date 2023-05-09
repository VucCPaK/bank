package com.example.back.queries;

import com.example.back.DTO.CardDTO;
import com.example.back.domain.CardAggregate;
import com.example.back.domain.CardDocument;
import com.example.back.es.EventStore;
import com.example.back.mappers.CardMapper;
import com.example.back.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
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
        Optional<CardDocument> optionalCardDocument = cardRepository.findCardDocumentByAggregateId(query.aggregateId());
        if (optionalCardDocument.isPresent()) {
            return optionalCardDocument.get().getBalance();
        }

        return eventStore.calculateBalance(query.aggregateId());
    }
}
