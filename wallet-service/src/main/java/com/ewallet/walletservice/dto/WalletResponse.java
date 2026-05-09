package com.ewallet.walletservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class WalletResponse {
    private String id;
    private String walletNumber;
    private BigDecimal balance;
    private String currency;
    private boolean active;
    private LocalDateTime createdAt;
    private String ownerEmail;
}