package com.aivarsliepa.budgetappapi.data.wallet;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WalletData {
    private Long id;

    @NotBlank
    private String name;
}
