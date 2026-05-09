package com.ewallet.notificationservice.dto;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class NotificationRequest {
    private String id;
    private String senderEmail;
    private String receiverEmail;
    private BigDecimal amount;
    private String description;
    private String status;
    private String type;
    private String referenceId;
}