package com.aivarsliepa.budgetappapi.data.repositories;

import com.aivarsliepa.budgetappapi.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
