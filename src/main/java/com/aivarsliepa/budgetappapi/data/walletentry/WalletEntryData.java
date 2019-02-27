package com.aivarsliepa.budgetappapi.data.walletentry;

import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class WalletEntryData implements WalletEntry {
    private Long id;

    @NotNull
    private Long categoryId;

    @NotNull
    private Date date;

    private String description;

    @NotNull
    private CategoryType type;
}
