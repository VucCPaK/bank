package com.example.back.commands;

public interface CardCommandService {
    void handle(DepositAmountCommand command);

    void handle(WithdrawAmountCommand command);

    String handle(CreateCardCommand command);
}
