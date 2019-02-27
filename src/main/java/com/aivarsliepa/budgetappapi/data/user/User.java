package com.aivarsliepa.budgetappapi.data.user;

import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<WalletModel> wallets = new ArrayList<>();
}