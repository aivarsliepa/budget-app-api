package com.aivarsliepa.budgetappapi.data.walletentry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletEntryRepository extends JpaRepository<WalletEntryModel, Long> {
    List<WalletEntryModel> findAllByWalletId(Long walletId);

    Optional<WalletEntryModel> findByIdAndWalletId(Long entryId, Long walletId);

    boolean existsByIdAndWalletId(Long entryId, Long walletId);

    void deleteByIdAndWalletId(Long entryId, Long walletId);
}
