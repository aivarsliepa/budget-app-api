package com.aivarsliepa.budgetappapi.data.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<WalletModel, Long> {
    List<WalletModel> findAllByUserId(Long userId);

    Optional<WalletModel> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
