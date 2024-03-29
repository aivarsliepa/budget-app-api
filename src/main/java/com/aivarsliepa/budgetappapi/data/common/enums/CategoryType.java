package com.aivarsliepa.budgetappapi.data.common.enums;

import lombok.Getter;
import lombok.NonNull;

public enum CategoryType {
    EXPENSE(0),
    INCOME(1);

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
