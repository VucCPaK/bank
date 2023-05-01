package com.example.back.commands;

import java.math.BigDecimal;

public record WithdrawAmountCommand(String aggregateId, BigDecimal amount) {
}
