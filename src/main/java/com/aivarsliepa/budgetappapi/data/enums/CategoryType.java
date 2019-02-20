package com.aivarsliepa.budgetappapi.data.enums;

import lombok.Getter;
import lombok.NonNull;

public enum CategoryType {
    EXPENSE(1),
    INCOME(2);

    @Getter
    private int id;

    CategoryType(int id) {
        this.id = id;
    }

    public static CategoryType getType(@NonNull Integer id) {
        for (CategoryType type : CategoryType.values()) {
            if (id.equals(type.getId())) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matching type for id: " + id);
    }
}
