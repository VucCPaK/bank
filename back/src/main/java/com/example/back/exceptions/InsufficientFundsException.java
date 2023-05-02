package com.example.back.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal balance, BigDecimal withdrawAmount) {
        super("\nBalance " + balance.toString() + " less than withdraw amount " + withdrawAmount.toString());
    }
}
