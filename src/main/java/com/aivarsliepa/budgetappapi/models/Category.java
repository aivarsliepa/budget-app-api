package com.aivarsliepa.budgetappapi.models;

import com.aivarsliepa.budgetappapi.models.enums.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "parent_id")
    private Long parentId;

    public CategoryType getType() {
        return CategoryType.getType(this.type);
    }

    public void setType(CategoryType type) {
        this.type = type.getId();
    }
}
