package com.ewallet.transactionservice.dto;

import com.ewallet.transactionservice.model.Transaction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class TransactionResponse {
    private String id;
    private String senderEmail;
    private String receiverEmail;
    private BigDecimal amount;
    private String description;
    private Transaction.TransactionStatus status;
    private Transaction.TransactionType type;
    private LocalDateTime createdAt;
    private String referenceId;
}