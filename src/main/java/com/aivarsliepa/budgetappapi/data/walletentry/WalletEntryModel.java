package com.aivarsliepa.budgetappapi.data.walletentry;


import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "wallet_entries")
public class WalletEntryModel implements WalletEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "type", nullable = false)
    private CategoryType type;
}
