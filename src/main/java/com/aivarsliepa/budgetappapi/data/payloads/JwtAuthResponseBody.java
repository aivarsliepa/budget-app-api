package com.aivarsliepa.budgetappapi.data.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthResponseBody {
    private String token;
}
