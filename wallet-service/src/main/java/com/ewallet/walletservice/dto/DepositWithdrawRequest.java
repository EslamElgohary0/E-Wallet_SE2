package com.ewallet.walletservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositWithdrawRequest {

    @NotNull
    @DecimalMin(value = "0.01", message = "المبلغ لازم يكون أكبر من صفر")
    private BigDecimal amount;

    private String description;
}