package com.aivarsliepa.budgetappapi.repositories;

import com.aivarsliepa.budgetappapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
