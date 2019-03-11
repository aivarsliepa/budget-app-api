package com.aivarsliepa.budgetappapi.data.walletentry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletEntryRepository extends JpaRepository<WalletEntryModel, Long> {
    List<WalletEntryModel> findAllByUserIdAndWalletId(Long userId, Long walletId);

    Optional<WalletEntryModel> findByIdAndUserIdAndWalletId(Long id, Long userId, Long walletId);

    boolean existsByIdAndUserIdAndWalletId(Long id, Long userId, Long walletId);

    void deleteByIdAndUserIdAndWalletId(Long id, Long userId, Long walletId);
}
