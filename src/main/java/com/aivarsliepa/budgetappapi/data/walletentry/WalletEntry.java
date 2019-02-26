package com.aivarsliepa.budgetappapi.data.walletentry;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;

import java.util.Date;

public interface WalletEntry {

    Long getId();

    void setId(Long id);

    Long getCategoryId();

    void setCategoryId(Long categoryId);

    Date getDate();

    void setDate(Date date);

    String getDescription();

    void setDescription(String description);

    CategoryType getType();

    void setType(CategoryType type);
}
