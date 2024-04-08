package ru.example.wallet.mapper;

import ru.example.wallet.dto.WalletDto;
import ru.example.wallet.model.Wallet;

public class WalletMapper {
    public static Wallet mapToWallet(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setId(walletDto.getId());
        wallet.setAmount(walletDto.getAmount());
        return wallet;
    }

    public static WalletDto mapToWalletDto(Wallet wallet) {
        return new WalletDto(wallet.getId(), wallet.getAmount());
    }
}
