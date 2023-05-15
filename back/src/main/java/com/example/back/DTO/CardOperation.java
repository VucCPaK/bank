package com.example.back.DTO;

import com.example.back.events.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardOperation(EventType eventType, BigDecimal amount, LocalDateTime timestamp) {
}
