package com.aivarsliepa.budgetappapi.data.user;

import com.aivarsliepa.budgetappapi.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
