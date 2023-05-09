package com.example.back.mappers;

import com.example.back.DTO.CardDTO;
import com.example.back.domain.CardAggregate;
import com.example.back.domain.CardDocument;

public class CardMapper {
    private CardMapper() {}

    public static CardDTO cardDTOFromCardDocument(CardDocument cardDocument) {
        return new CardDTO(
                cardDocument.getAggregateId(),
                cardDocument.getCurrency(),
                cardDocument.getBalance(),
                cardDocument.getExpirationDate(),
                cardDocument.getCVC(),
                cardDocument.getCustomerId()
        );
    }

    public static CardDTO cardDTOFromCardAggregate(CardAggregate cardAggregate) {
        return new CardDTO(
                cardAggregate.getId(),
                cardAggregate.getCurrency(),
                cardAggregate.getBalance(),
                cardAggregate.getExpirationDate(),
                cardAggregate.getCVC(),
                cardAggregate.getCustomerId()
        );
    }

    public static CardDocument cardDocumentFromCardAggregate(CardAggregate aggregate) {
        return CardDocument.builder()
                .aggregateId(aggregate.getId())
                .currency(aggregate.getCurrency())
                .CVC(aggregate.getCVC())
                .expirationDate(aggregate.getExpirationDate())
                .customerId(aggregate.getCustomerId())
                .balance(aggregate.getBalance())
                .build();
    }
}
