package com.ewallet.transactionservice.feign;

import com.ewallet.transactionservice.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationFeignClient {

    @PostMapping("/api/notifications/internal/create")
    void sendNotification(@RequestBody TransactionResponse transaction);
}