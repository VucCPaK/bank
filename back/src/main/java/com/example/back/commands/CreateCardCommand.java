package com.example.back.commands;

import com.example.back.es.Currency;

import java.time.LocalDateTime;

public record CreateCardCommand(String aggregateId, Currency currency,
                                String customerId, LocalDateTime expirationDate, String CVC) {
}
