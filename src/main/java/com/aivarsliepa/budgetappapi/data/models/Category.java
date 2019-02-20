package com.aivarsliepa.budgetappapi.data.models;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Min(1)
    @Max(2)
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
