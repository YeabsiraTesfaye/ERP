package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InventoryTransactionLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private InventoryTransaction transaction;

    @ManyToOne
    private Product product;

    private Double quantity;
    private Double unitPrice;

    @ManyToOne
    private ChartOfAccount expenseOrRevenueAccount;
}
