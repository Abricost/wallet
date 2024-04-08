package ru.example.wallet.exception;

public class WalletUnsupportedOperationException extends RuntimeException {
    public WalletUnsupportedOperationException(final String message) {
        super(message);
    }
}
