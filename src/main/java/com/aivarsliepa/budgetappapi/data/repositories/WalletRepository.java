package com.aivarsliepa.budgetappapi.data.repositories;

import com.aivarsliepa.budgetappapi.data.models.WalletModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletModel, Long> {
}
