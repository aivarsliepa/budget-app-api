package com.aivarsliepa.budgetappapi.data.walletentry;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class WalletEntryData implements WalletEntry {
    private Long id;

    private Long categoryId;

    private Date date;

    private String description;

    @NotNull
    private CategoryType type;
}
