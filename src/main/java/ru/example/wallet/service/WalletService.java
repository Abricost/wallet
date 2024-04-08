package ru.example.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.wallet.controller.request.OperationType;
import ru.example.wallet.controller.request.WalletRequest;
import ru.example.wallet.dto.WalletDto;
import ru.example.wallet.exception.WalletNotEnoughFundsException;
import ru.example.wallet.exception.WalletNotFoundException;
import ru.example.wallet.exception.WalletUnsupportedOperationException;
import ru.example.wallet.mapper.WalletMapper;
import ru.example.wallet.model.Wallet;
import ru.example.wallet.repository.WalletRepository;

import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public WalletDto getWalletBalance(UUID walletId) throws Exception {
        return WalletMapper.mapToWalletDto(walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found."))) ;
    }

    @Transactional()
    public void editWalletBalance(WalletRequest walletRequest) throws Exception {
        if (!walletRepository.existsById(walletRequest.getValletId())) throw new WalletNotFoundException("Wallet not found.");

        if (walletRequest.getOperationType() == OperationType.DEPOSIT) {
            walletRepository.addAmount(walletRequest.getValletId(), walletRequest.getAmount());
        } else if (walletRequest.getOperationType() == OperationType.WITHDRAW) {
            if (walletRepository.findById(walletRequest.getValletId()).get().getAmount() >= walletRequest.getAmount()) {
                walletRepository.addAmount(walletRequest.getValletId(), walletRequest.getAmount() * -1.0);
            } else {
                throw new WalletNotEnoughFundsException("Not enough funds in your wallet");
            }
        } else {
            throw new WalletUnsupportedOperationException("Unsupported operation type");
        }
    }
}
