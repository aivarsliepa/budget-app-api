package com.aivarsliepa.budgetappapi.repositories;

import com.aivarsliepa.budgetappapi.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
