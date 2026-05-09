package com.ewallet.transactionservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String senderEmail;
    private String receiverEmail;
    private BigDecimal amount;
    private String description;

    @Builder.Default
    private TransactionType type = TransactionType.TRANSFER;

    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String referenceId;

    public enum TransactionType  { TRANSFER, DEPOSIT, WITHDRAWAL }
    public enum TransactionStatus { PENDING, COMPLETED, FAILED }
}