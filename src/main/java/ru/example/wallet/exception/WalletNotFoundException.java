package ru.example.wallet.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(final String message) {
        super(message);
    }
}
