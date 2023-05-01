package com.example.back.controllers;

import com.example.back.commands.CreateCardCommand;
import com.example.back.commands.DepositAmountCommand;
import com.example.back.commands.WithdrawAmountCommand;
import com.example.back.es.Currency;
import com.example.back.commands.CardCommandService;
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
//    private final CardQueryService queryService;

//    @GetMapping("{id}")
//    public Card getCard(@PathVariable long id) {
//        final var card = cardService.getCard(id);
//        log.info("Get card result: {}", card);
//        return cardService.getCard(id);
//    }

//    @GetMapping("/cardByCustomerId/{customerId}")
//    public Card getCardByCustomerId(@PathVariable String customerId) {
//        return cardService.getCardByCustomerId(customerId);
//    }

//    @GetMapping("all")
//    public List<Card> getAllCards() {
//        final var listOfCards = cardService.getAllCards();
//        log.info("Get all cards result: {}", listOfCards);
//        return listOfCards;
//    }

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

//    @GetMapping("{id}/balance")
//    public double getBalance(@PathVariable long id) {
//        final var balance = cardService.getBalance(id);
//        log.info("Balance {} of card {}", balance, id);
//        return cardService.getBalance(id);
//    }
}
