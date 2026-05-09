package com.ewallet.transactionservice.controller;

import com.ewallet.transactionservice.dto.TransactionResponse;
import com.ewallet.transactionservice.model.Transaction;
import com.ewallet.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/transactions/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminTransactionController {

    private final TransactionRepository transactionRepository;


    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        log.info("[ADMIN] Fetching all transactions");
        List<TransactionResponse> result = transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(
            @PathVariable String email) {
        List<TransactionResponse> result = transactionRepository
                .findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(email, email)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTransactionStats() {
        List<Transaction> all = transactionRepository.findAll();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTransactions", all.size());
        stats.put("completed", all.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.COMPLETED).count());
        stats.put("failed", all.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.FAILED).count());
        stats.put("pending", all.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.PENDING).count());
        BigDecimal totalVolume = all.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.COMPLETED)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalVolumeTransferred", totalVolume);
        return ResponseEntity.ok(stats);
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .senderEmail(t.getSenderEmail())
                .receiverEmail(t.getReceiverEmail())
                .amount(t.getAmount())
                .description(t.getDescription())
                .status(t.getStatus())
                .type(t.getType())
                .createdAt(t.getCreatedAt())
                .referenceId(t.getReferenceId())
                .build();
    }
}