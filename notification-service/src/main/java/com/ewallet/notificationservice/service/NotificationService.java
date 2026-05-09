package com.ewallet.notificationservice.service;

import com.ewallet.notificationservice.dto.NotificationRequest;
import com.ewallet.notificationservice.model.Notification;
import com.ewallet.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * يستقبل من Transaction Service ويعمل إشعارين:
     * 1. للمرسل: "أرسلت X جنيه"
     * 2. للمستقبل: "استقبلت X جنيه"
     */
    public void createNotificationsForTransaction(NotificationRequest req) {
        log.info("[NOTIFICATION] Creating notifications for tx: {}", req.getReferenceId());

        // إشعار للمرسل
        Notification senderNotif = Notification.builder()
                .recipientEmail(req.getSenderEmail())
                .title("تم إرسال تحويل")
                .message(String.format("أرسلت %.2f EGP إلى %s",
                        req.getAmount(), req.getReceiverEmail()))
                .transactionId(req.getId())
                .referenceId(req.getReferenceId())
                .amount(req.getAmount())
                .type("SENT")
                .build();

        // إشعار للمستقبل
        Notification receiverNotif = Notification.builder()
                .recipientEmail(req.getReceiverEmail())
                .title("تم استقبال تحويل")
                .message(String.format("استقبلت %.2f EGP من %s",
                        req.getAmount(), req.getSenderEmail()))
                .transactionId(req.getId())
                .referenceId(req.getReferenceId())
                .amount(req.getAmount())
                .type("RECEIVED")
                .build();

        notificationRepository.save(senderNotif);
        notificationRepository.save(receiverNotif);
        log.info("[NOTIFICATION] Saved 2 notifications for tx {}", req.getReferenceId());
    }

    /** جلب إشعارات المستخدم */
    public List<Notification> getMyNotifications(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    /** جلب الإشعارات غير المقروءة */
    public List<Notification> getUnreadNotifications(String email) {
        return notificationRepository.findByRecipientEmailAndRead(email, false);
    }

    /** تعليم الإشعار كمقروء */
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}