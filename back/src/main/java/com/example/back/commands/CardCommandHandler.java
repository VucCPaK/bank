package com.example.back.commands;

import com.example.back.domain.CardAggregate;
import com.example.back.es.EventStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CardCommandHandler implements CardCommandService {

    private final EventStore eventStore;

    @Override
    public void handle(DepositAmountCommand command) {
        var aggregate = eventStore.load(command.aggregateId(), CardAggregate.class);
        aggregate.depositAmount(command.amount());
        eventStore.save(aggregate);
        log.info("(DepositAmountCommand) aggregate: {}", aggregate);
    }

    @Override
    public void handle(WithdrawAmountCommand command) {
        var aggregate = eventStore.load(command.aggregateId(), CardAggregate.class);
        aggregate.withdrawAmount(command.amount());
        eventStore.save(aggregate);
        log.info("(DepositAmountCommand) aggregate: {}", aggregate);
    }

    @Override
    public String handle(CreateCardCommand command) {
        var aggregate = new CardAggregate(command.aggregateId());
        aggregate.createCard(command.currency(), command.customerId(), command.expirationDate(), command.CVC());
        eventStore.save(aggregate);

        log.info("(CreateCardCommand aggregate: {}", aggregate);
        return aggregate.getId();
    }
}
