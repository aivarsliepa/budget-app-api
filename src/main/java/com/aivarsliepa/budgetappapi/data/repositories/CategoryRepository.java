package com.aivarsliepa.budgetappapi.data.repositories;

import com.aivarsliepa.budgetappapi.data.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
