package com.example.back.DTO;

import com.example.back.es.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardDTO(String aggregateId, Currency currency, BigDecimal balance,
                      LocalDateTime expirationDate, String CVC, String customerId) {
}
