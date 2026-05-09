package com.ewallet.walletservice.controller;

import com.ewallet.walletservice.dto.DepositWithdrawRequest;
import com.ewallet.walletservice.dto.WalletResponse;
import com.ewallet.walletservice.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(Authentication auth) {

        String email = auth.getName();
        WalletResponse resp = walletService.createWallet(email, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping("/me")
    public ResponseEntity<WalletResponse> getMyWallet(Authentication auth) {
        return ResponseEntity.ok(walletService.getMyWallet(auth.getName()));
    }


    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(
            Authentication auth,
            @Valid @RequestBody DepositWithdrawRequest req) {
        return ResponseEntity.ok(walletService.deposit(auth.getName(), req));
    }


    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(
            Authentication auth,
            @Valid @RequestBody DepositWithdrawRequest req) {
        return ResponseEntity.ok(walletService.withdraw(auth.getName(), req));
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Wallet Service UP ✓");
    }
}