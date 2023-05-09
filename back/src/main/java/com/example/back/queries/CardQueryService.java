package com.example.back.queries;

import com.example.back.DTO.CardDTO;

import java.math.BigDecimal;

public interface CardQueryService {

    CardDTO handle(GetCardByAggregateId query);

    CardDTO handle(GetCardByCustomerId query);

    BigDecimal handle(GetCardBalanceByAggregateId query);
}
