package com.ewallet.walletservice.service;

import com.ewallet.walletservice.dto.DepositWithdrawRequest;
import com.ewallet.walletservice.dto.WalletResponse;
import com.ewallet.walletservice.model.Wallet;
import com.ewallet.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    /** إنشاء محفظة جديدة للمستخدم */
    public WalletResponse createWallet(String userId, String email) {
        if (walletRepository.existsByUserId(userId)) {
            throw new RuntimeException("المستخدم لديه محفظة مسبقاً");
        }
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .walletNumber(generateWalletNumber())
                .ownerEmail(email)
                .build();
        return toResponse(walletRepository.save(wallet));
    }

    /** جلب محفظة المستخدم */
    public WalletResponse getMyWallet(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("المحفظة غير موجودة"));
        return toResponse(wallet);
    }

    /** إيداع */
    public WalletResponse deposit(String userId, DepositWithdrawRequest req) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("المحفظة غير موجودة"));
        wallet.setBalance(wallet.getBalance().add(req.getAmount()));
        return toResponse(walletRepository.save(wallet));
    }

    /** سحب */
    public WalletResponse withdraw(String userId, DepositWithdrawRequest req) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("المحفظة غير موجودة"));
        if (wallet.getBalance().compareTo(req.getAmount()) < 0) {
            throw new RuntimeException("الرصيد غير كافٍ");
        }
        wallet.setBalance(wallet.getBalance().subtract(req.getAmount()));
        return toResponse(walletRepository.save(wallet));
    }

    /** استخدمه من Transaction Service عشان يخصم أو يضيف */
    public void updateBalanceByUserId(String userId, BigDecimal amount, boolean isCredit) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("المحفظة غير موجودة"));
        if (isCredit) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            if (wallet.getBalance().compareTo(amount) < 0)
                throw new RuntimeException("الرصيد غير كافٍ");
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }
        walletRepository.save(wallet);
    }

    private String generateWalletNumber() {
        return "EW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private WalletResponse toResponse(Wallet w) {
        return WalletResponse.builder()
                .id(w.getId()).walletNumber(w.getWalletNumber())
                .balance(w.getBalance()).currency(w.getCurrency())
                .active(w.isActive()).createdAt(w.getCreatedAt())
                .ownerEmail(w.getOwnerEmail()).build();
    }
}