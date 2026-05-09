package com.ewallet.transactionservice.service;

import com.ewallet.transactionservice.dto.TransactionResponse;
import com.ewallet.transactionservice.dto.TransferRequest;
import com.ewallet.transactionservice.feign.NotificationFeignClient;
import com.ewallet.transactionservice.feign.WalletFeignClient;
import com.ewallet.transactionservice.model.Transaction;
import com.ewallet.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletFeignClient walletFeignClient;
    private final NotificationFeignClient notificationFeignClient;


    public TransactionResponse transfer(String senderEmail, TransferRequest req) {
        String refId = UUID.randomUUID().toString();

        Transaction tx = Transaction.builder()
                .senderEmail(senderEmail)
                .receiverEmail(req.getReceiverEmail())
                .amount(req.getAmount())
                .description(req.getDescription())
                .type(Transaction.TransactionType.TRANSFER)
                .referenceId(refId)
                .build();

        try {

            walletFeignClient.updateBalance(senderEmail, req.getAmount(), false);

            walletFeignClient.updateBalance(req.getReceiverEmail(), req.getAmount(), true);

            tx.setStatus(Transaction.TransactionStatus.COMPLETED);
            Transaction saved = transactionRepository.save(tx);
            TransactionResponse response = toResponse(saved);


            try {
                notificationFeignClient.sendNotification(response);
            } catch (Exception e) {
                log.warn("Notification failed: {}", e.getMessage());
            }
            return response;
        } catch (Exception e) {
            tx.setStatus(Transaction.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("فشلت عملية التحويل: " + e.getMessage());
        }
    }


    public List<TransactionResponse> getMyTransactions(String email) {
        return transactionRepository
                .findBySenderEmailOrReceiverEmailOrderByCreatedAtDesc(email, email)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId()).senderEmail(t.getSenderEmail())
                .receiverEmail(t.getReceiverEmail()).amount(t.getAmount())
                .description(t.getDescription()).status(t.getStatus())
                .type(t.getType()).createdAt(t.getCreatedAt())
                .referenceId(t.getReferenceId()).build();
    }
}