package com.aivarsliepa.budgetappapi.data.walletentry;


import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wallet_entries")
public class WalletEntryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, updatable = false, insertable = false)
    private WalletModel wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, updatable = false, insertable = false)
    private CategoryModel category;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "type", nullable = false)
    private CategoryType type;

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        var categoryUserId = category.getUser().getId();
        var walletUserId = wallet.getUserId();
        var userId = user.getId();

        if (!userId.equals(walletUserId)) {
            var msg = "WalletEntryModel userID: '" + userId + "' does not match with walletUserID: " + walletUserId;
            throw new DataIntegrityViolationException(msg);
        }

        if (!userId.equals(categoryUserId)) {
            var msg = "WalletEntryModel userID: '" + userId + "' does not match with categoryUserID: " + categoryUserId;
            throw new DataIntegrityViolationException(msg);
        }
    }
}
