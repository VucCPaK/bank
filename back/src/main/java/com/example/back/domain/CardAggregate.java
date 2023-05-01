package com.example.back.domain;

import com.example.back.es.Currency;
import com.example.back.es.AggregateRoot;
import com.example.back.es.Event;
import com.example.back.es.SerializeUtils;
import com.example.back.events.CreateCardEvent;
import com.example.back.events.DepositAmountEvent;
import com.example.back.events.EventType;
import com.example.back.events.WithdrawAmountEvent;
import com.example.back.exceptions.InsufficientFundsException;
import com.example.back.exceptions.InvalidEventTypeException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardAggregate extends AggregateRoot {

    public static final String AGGREGATE_TYPE = "CardAggregate";

    private Currency currency;
    private BigDecimal balance;
    private LocalDateTime expirationDate;
    private String CVC;
    private String customerId;

    public CardAggregate(String id) {
        super(id, AGGREGATE_TYPE);
    }

    @Override
    public void when(Event event) {
        switch (event.getEventType()) {
            case CREATE_CARD ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), CreateCardEvent.class));
            case DEPOSIT_AMOUNT ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), DepositAmountEvent.class));
            case WITHDRAW_AMOUNT ->
                    handle(SerializeUtils.deserializeFromJsonBytes(event.getData(), WithdrawAmountEvent.class));
            default -> throw new InvalidEventTypeException(event.toString());
        }
    }

    private void handle(CreateCardEvent event) {
        this.currency = event.getCurrency();
        this.balance = BigDecimal.valueOf(0);
        this.expirationDate = event.getExpirationDate();
        this.CVC = event.getCVC();
        this.customerId = event.getCustomerId();
    }

    private void handle(DepositAmountEvent event) {
        this.balance = this.balance.add(event.getDepositAmount());
    }

    private void handle(WithdrawAmountEvent event) {
        if (event.getWithdrawAmount().compareTo(this.balance) > 0)
            throw new InsufficientFundsException(this.balance, event.getWithdrawAmount());

        this.balance = this.balance.subtract(event.getWithdrawAmount());
    }

    public void createCard(Currency currency, String customerId, LocalDateTime expirationDate, String CVC) {
        var data = new CreateCardEvent(this.id, currency, customerId, expirationDate, CVC);
        byte[] dataBytes = SerializeUtils.serializeToJsonBytes(data);
        var event = this.createEvent(EventType.CREATE_CARD, dataBytes, null);
        this.apply(event);
    }

    public void depositAmount(BigDecimal depositAmount) {
        var data = new DepositAmountEvent(this.id, depositAmount);
        byte[] dataBytes = SerializeUtils.serializeToJsonBytes(data);
        var event = this.createEvent(EventType.DEPOSIT_AMOUNT, dataBytes, null);
        this.apply(event);
    }

    public void withdrawAmount(BigDecimal withdrawAmount) {
        var data = new WithdrawAmountEvent(this.id, withdrawAmount);
        byte[] dataBytes = SerializeUtils.serializeToJsonBytes(data);
        var event = this.createEvent(EventType.WITHDRAW_AMOUNT, dataBytes, null);
        this.apply(event);
    }
}
