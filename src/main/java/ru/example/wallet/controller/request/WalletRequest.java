package ru.example.wallet.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WalletRequest {
    private UUID valletId;
    private OperationType operationType;
    private Double amount;
}
