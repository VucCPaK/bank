package com.example.back.events;

import com.example.back.es.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawAmountEvent extends BaseEvent {
    private BigDecimal withdrawAmount;

    public WithdrawAmountEvent(String aggregateId, BigDecimal withdrawAmount) {
        super(aggregateId);
        this.withdrawAmount = withdrawAmount;
    }
}
