package com.aivarsliepa.budgetappapi.data.walletentry;


import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletModel wallet;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "type", nullable = false)
    private CategoryType type;
}
