package com.aivarsliepa.budgetappapi.data.category;

import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
