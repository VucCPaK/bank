package com.example.back.controllers;

import com.example.back.DTO.CardDTO;
import com.example.back.DTO.TransactionsHistory;
import com.example.back.commands.CreateCardCommand;
import com.example.back.commands.DepositAmountCommand;
import com.example.back.commands.WithdrawAmountCommand;
import com.example.back.es.Currency;
import com.example.back.commands.CardCommandService;
import com.example.back.queries.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardCommandService commandService;
    private final CardQueryService queryService;

    @GetMapping("{aggregateId}")
    public ResponseEntity<CardDTO> getCardByAggregateId(@PathVariable String aggregateId) {
        var cardDTO = queryService.handle(new GetCardByAggregateId(aggregateId));
        log.info("(getCardByAggregateId) get card: {}", cardDTO);
        return ResponseEntity.ok(cardDTO);
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity<CardDTO> getCardByCustomerId(@PathVariable String customerId) {
        var cardDTO = queryService.handle(new GetCardByCustomerId(customerId));

        if (cardDTO == null) {
            return ResponseEntity.notFound().build();
        }

        log.info("(getCardByCustomerId) get card: {}", cardDTO);
        return ResponseEntity.ok(cardDTO);
    }

    @GetMapping("{aggregateId}/balance")
    public ResponseEntity<BigDecimal> getCardBalanceByAggregateId(@PathVariable String aggregateId) {
        var balance = queryService.handle(new GetCardBalanceByAggregateId(aggregateId));
        log.info("(getCardBalanceByAggregateId) card: {}, balance : {}", aggregateId, balance);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("{aggregateId}/history")
    public ResponseEntity<TransactionsHistory> getTransactionsHistory(@PathVariable String aggregateId) {
        var history = queryService.handle(new GetTransactionsHistory(aggregateId));
        log.info("(getTransactionsHistory) history: {}", history);
        return ResponseEntity.ok(history);
    }

    @PostMapping("new")
    public ResponseEntity<String> createCard(@RequestParam Currency currency, @RequestParam String customerId) {
        var aggregateId = UUID.randomUUID().toString();
        var expirationDate = LocalDateTime.now().plusYears(4); // current date + 4 years
        var CVC = String.format("%03d", (int) (Math.random() * 1000)); // get concatenated 3 random digit

        var id = commandService.handle(new CreateCardCommand(
                aggregateId, currency, customerId, expirationDate, CVC));

        log.info("Card created with id: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PostMapping("{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable String id, @RequestParam BigDecimal amount) {
        commandService.handle(new DepositAmountCommand(id, amount));
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable String id, @RequestParam BigDecimal amount) {
        commandService.handle(new WithdrawAmountCommand(id, amount));
        return ResponseEntity.ok().build();
    }
}
