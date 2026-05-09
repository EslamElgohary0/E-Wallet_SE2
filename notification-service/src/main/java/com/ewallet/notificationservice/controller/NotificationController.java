package com.ewallet.notificationservice.controller;

import com.ewallet.notificationservice.dto.NotificationRequest;
import com.ewallet.notificationservice.model.Notification;
import com.ewallet.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/internal/create")
    public ResponseEntity<Void> createNotification(
            @RequestBody NotificationRequest req) {
        notificationService.createNotificationsForTransaction(req);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/my")
    public ResponseEntity<List<Notification>> myNotifications(Authentication auth) {
        return ResponseEntity.ok(notificationService.getMyNotifications(auth.getName()));
    }


    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> unread(Authentication auth) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(auth.getName()));
    }


    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service UP ✓");
    }
}