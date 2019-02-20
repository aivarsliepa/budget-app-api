package com.aivarsliepa.budgetappapi.data.repositories;

import com.aivarsliepa.budgetappapi.data.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByWalletId(Long walletId);

    Optional<Income> findByIdAndWalletId(Long incomeId, Long walletId);

    boolean existsByIdAndWalletId(Long incomeId, Long walletId);
}
