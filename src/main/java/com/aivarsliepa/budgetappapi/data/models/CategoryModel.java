package com.aivarsliepa.budgetappapi.data.models;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Column(name = "parent_id")
    private Long parentId;
}
