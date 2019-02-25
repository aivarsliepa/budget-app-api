package com.aivarsliepa.budgetappapi.data.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WalletData {
    private Long id;

    @NotEmpty
    private String name;
}
