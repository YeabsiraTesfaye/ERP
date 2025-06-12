package com.yab.multitenantERP.entity;

import jakarta.persistence.*;

@Entity
public class InventoryAccountMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ChartOfAccount purchaseAccount;

    @ManyToOne
    private ChartOfAccount salesAccount;

    @ManyToOne
    private ChartOfAccount inventoryAccount;
}
