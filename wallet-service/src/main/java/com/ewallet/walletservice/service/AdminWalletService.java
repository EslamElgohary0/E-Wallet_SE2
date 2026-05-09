package com.ewallet.walletservice.service;

import com.ewallet.walletservice.dto.WalletResponse;
import com.ewallet.walletservice.model.Wallet;
import com.ewallet.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminWalletService {

    private final WalletRepository walletRepository;


    public List<WalletResponse> getAllWallets() {
        log.info("[ADMIN] Fetching all wallets");
        return walletRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }


    public WalletResponse getWalletByUserId(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("لا توجد محفظة للـ userId: " + userId));
        return toResponse(wallet);
    }


    public WalletResponse getWalletByNumber(String walletNumber) {
        Wallet wallet = walletRepository.findByWalletNumber(walletNumber)
                .orElseThrow(() -> new RuntimeException("لا توجد محفظة برقم: " + walletNumber));
        return toResponse(wallet);
    }


    public WalletResponse freezeWallet(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("لا توجد محفظة للمستخدم"));
        wallet.setActive(false);
        log.info("[ADMIN] Froze wallet for user: {}", userId);
        return toResponse(walletRepository.save(wallet));
    }


    public WalletResponse unfreezeWallet(String userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("لا توجد محفظة للمستخدم"));
        wallet.setActive(true);
        log.info("[ADMIN] Unfroze wallet for user: {}", userId);
        return toResponse(walletRepository.save(wallet));
    }


    public Map<String, Object> getWalletStats() {
        List<Wallet> allWallets = walletRepository.findAll();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalWallets", allWallets.size());
        stats.put("activeWallets", allWallets.stream().filter(Wallet::isActive).count());
        stats.put("frozenWallets", allWallets.stream().filter(w -> !w.isActive()).count());
        BigDecimal totalBalance = allWallets.stream()
                .map(Wallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalBalanceInSystem", totalBalance);
        stats.put("averageBalance", allWallets.isEmpty() ? BigDecimal.ZERO :
                totalBalance.divide(BigDecimal.valueOf(allWallets.size()), 2,
                        java.math.RoundingMode.HALF_UP));
        return stats;
    }

    private WalletResponse toResponse(Wallet w) {
        return WalletResponse.builder()
                .id(w.getId())
                .walletNumber(w.getWalletNumber())
                .balance(w.getBalance())
                .currency(w.getCurrency())
                .active(w.isActive())
                .createdAt(w.getCreatedAt())
                .ownerEmail(w.getOwnerEmail())
                .build();
    }
}