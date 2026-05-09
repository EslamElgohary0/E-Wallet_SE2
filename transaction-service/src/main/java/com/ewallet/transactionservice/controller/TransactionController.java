package com.ewallet.transactionservice.controller;

import com.ewallet.transactionservice.dto.TransactionResponse;
import com.ewallet.transactionservice.dto.TransferRequest;
import com.ewallet.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            Authentication auth,
            @Valid @RequestBody TransferRequest req) {
        return ResponseEntity.ok(transactionService.transfer(auth.getName(), req));
    }


    @GetMapping("/my")
    public ResponseEntity<List<TransactionResponse>> myTransactions(Authentication auth) {
        return ResponseEntity.ok(transactionService.getMyTransactions(auth.getName()));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Transaction Service UP ✓");
    }
}