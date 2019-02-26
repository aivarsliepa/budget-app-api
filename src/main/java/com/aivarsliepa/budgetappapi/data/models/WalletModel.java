package com.aivarsliepa.budgetappapi.data.models;

import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "wallets")
public class WalletModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "walletId")
    private List<WalletEntryModel> entries = new ArrayList<>();

}
