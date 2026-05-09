package com.ewallet.walletservice.repository;

import com.ewallet.walletservice.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);
    Optional<Wallet> findByWalletNumber(String walletNumber);
    boolean existsByUserId(String userId);
}