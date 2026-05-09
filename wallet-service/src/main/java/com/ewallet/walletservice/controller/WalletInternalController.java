package com.ewallet.walletservice.controller;

import com.ewallet.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/wallet/internal")
@RequiredArgsConstructor
public class WalletInternalController {

    private final WalletService walletService;


    @PutMapping("/update-balance/{userId}")
    public ResponseEntity<Void> updateBalance(
            @PathVariable String userId,
            @RequestParam BigDecimal amount,
            @RequestParam boolean isCredit) {
        walletService.updateBalanceByUserId(userId, amount, isCredit);
        return ResponseEntity.ok().build();
    }
}