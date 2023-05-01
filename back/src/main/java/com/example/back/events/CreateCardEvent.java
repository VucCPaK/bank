package com.example.back.events;

import com.example.back.es.Currency;
import com.example.back.es.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateCardEvent extends BaseEvent {
    private Currency currency;
    private String customerId;
    private LocalDateTime expirationDate;
    private String CVC;

    public CreateCardEvent(String aggregateId, Currency currency,
                           String customerId, LocalDateTime expirationDate, String CVC) {

        super(aggregateId);
        this.currency = currency;
        this.customerId = customerId;
        this.expirationDate = expirationDate;
        this.CVC = CVC;
    }
}
