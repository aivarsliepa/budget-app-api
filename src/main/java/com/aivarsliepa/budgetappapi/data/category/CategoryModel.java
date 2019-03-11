package com.aivarsliepa.budgetappapi.data.category;

import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "categories")
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "parentId")
    private Set<CategoryModel> subCategories = new HashSet<>();

    @OneToMany(mappedBy = "categoryId")
    private Set<WalletEntryModel> entries = new HashSet<>();

    @PreUpdate
    @PrePersist
    public void prePersistOrUpdate() {
        // test if parent category is owned by the same user
        if (parentId != null && user.getCategories()
                                    .stream()
                                    .noneMatch(categoryModel -> categoryModel.getId().equals(parentId))) {
            throw new DataIntegrityViolationException("Invalid 'parentId': " + parentId);
        }
    }

    @PreRemove
    public void preRemove() {
        // set parentID for sub-categories, can be null, which is fine
        for (var subCategory : subCategories) {
            subCategory.setParentId(parentId);
        }

        // set category for entries referencing this category
        // TODO: think of what to do when parentID is null
        for (var entry : entries) {
            entry.setCategoryId(parentId);
        }
    }
}
