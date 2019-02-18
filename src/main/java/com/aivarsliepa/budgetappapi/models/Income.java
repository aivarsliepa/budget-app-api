package com.aivarsliepa.budgetappapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "income")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    @JsonIgnore
    private Long walletId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;
}
