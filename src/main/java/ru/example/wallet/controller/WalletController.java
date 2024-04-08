package ru.example.wallet.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.wallet.controller.request.WalletRequest;
import ru.example.wallet.exception.WalletNotEnoughFundsException;
import ru.example.wallet.exception.WalletNotFoundException;
import ru.example.wallet.exception.WalletUnsupportedOperationException;
import ru.example.wallet.service.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/")
    public ResponseEntity<?> getWalletBalance(@RequestParam UUID walletId) {
        try {
            return ResponseEntity.ok().body(walletService.getWalletBalance(walletId));
        } catch (WalletNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> editWalletBalance(@RequestBody WalletRequest walletRequest) {
        if (walletRequest.getAmount() <= 0) return ResponseEntity.badRequest().body("The amount must be greater than zero");
        try {
            walletService.editWalletBalance(walletRequest);
            return ResponseEntity.ok().body("success");
        } catch (WalletNotEnoughFundsException | WalletUnsupportedOperationException | WalletNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
