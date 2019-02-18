package com.aivarsliepa.budgetappapi.repositories;

import com.aivarsliepa.budgetappapi.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByWalletId(Long walletId);

    Optional<Expense> findByIdAndWalletId(Long expenseId, Long walletId);

    boolean existsByIdAndWalletId(Long expenseId, Long walletId);
}
