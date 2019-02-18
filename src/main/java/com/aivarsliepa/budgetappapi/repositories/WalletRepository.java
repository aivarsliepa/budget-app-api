package com.aivarsliepa.budgetappapi.repositories;

import com.aivarsliepa.budgetappapi.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
