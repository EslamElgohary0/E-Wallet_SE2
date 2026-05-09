package com.ewallet.walletservice.controller;

import com.ewallet.walletservice.dto.WalletResponse;
import com.ewallet.walletservice.service.AdminWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin-Only Wallet Controller
 * الـ Admin يقدر يشوف كل المحافظ ويدير العمليات
 */
@RestController
@RequestMapping("/api/wallet/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminWalletController {

    private final AdminWalletService adminWalletService;

    /**
     * GET /api/wallet/admin/all
     * جلب كل المحافظ في النظام
     */
    @GetMapping("/all")
    public ResponseEntity<List<WalletResponse>> getAllWallets() {
        return ResponseEntity.ok(adminWalletService.getAllWallets());
    }

    /**
     * GET /api/wallet/admin/user/{userId}
     * جلب محفظة مستخدم معين بالـ userId
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponse> getWalletByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(adminWalletService.getWalletByUserId(userId));
    }

    /**
     * GET /api/wallet/admin/number/{walletNumber}
     * جلب محفظة بالـ Wallet Number
     */
    @GetMapping("/number/{walletNumber}")
    public ResponseEntity<WalletResponse> getWalletByNumber(@PathVariable String walletNumber) {
        return ResponseEntity.ok(adminWalletService.getWalletByNumber(walletNumber));
    }

    /**
     * PUT /api/wallet/admin/{userId}/freeze
     * تجميد محفظة مستخدم (active = false)
     */
    @PutMapping("/{userId}/freeze")
    public ResponseEntity<WalletResponse> freezeWallet(@PathVariable String userId) {
        return ResponseEntity.ok(adminWalletService.freezeWallet(userId));
    }

    /**
     * PUT /api/wallet/admin/{userId}/unfreeze
     * إلغاء تجميد المحفظة (active = true)
     */
    @PutMapping("/{userId}/unfreeze")
    public ResponseEntity<WalletResponse> unfreezeWallet(@PathVariable String userId) {
        return ResponseEntity.ok(adminWalletService.unfreezeWallet(userId));
    }

    /**
     * GET /api/wallet/admin/stats
     * إحصائيات المحافظ
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getWalletStats() {
        return ResponseEntity.ok(adminWalletService.getWalletStats());
    }
}