package com.ewallet.transactionservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {

    @Email @NotNull
    private String receiverEmail;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String description;
}