package ru.example.wallet.exception;

public class WalletNotEnoughFundsException extends RuntimeException {
    public WalletNotEnoughFundsException(final String message) {
        super(message);
    }
}
