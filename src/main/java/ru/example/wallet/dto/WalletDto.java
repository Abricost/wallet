package ru.example.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WalletDto {
    private UUID id;
    private Double amount;
}
