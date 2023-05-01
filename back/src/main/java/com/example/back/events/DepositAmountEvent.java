package com.example.back.events;

import com.example.back.es.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepositAmountEvent extends BaseEvent {
    private BigDecimal depositAmount;

    public DepositAmountEvent(String aggregateId, BigDecimal depositAmount) {
        super(aggregateId);
        this.depositAmount = depositAmount;
    }
}
