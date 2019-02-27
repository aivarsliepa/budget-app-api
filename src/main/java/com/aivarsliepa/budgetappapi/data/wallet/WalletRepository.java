package com.aivarsliepa.budgetappapi.data.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletModel, Long> {
}
