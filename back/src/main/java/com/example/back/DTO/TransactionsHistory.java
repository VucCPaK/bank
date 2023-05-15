package com.example.back.DTO;

import java.util.List;

public record TransactionsHistory(List<CardOperation> cardOperations) {
}
