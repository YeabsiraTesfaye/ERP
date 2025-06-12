package com.yab.multitenantERP.entity;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private String description;

    @ManyToOne
    private UnitOfMeasure uom;

    private Double costPrice;
    private Double sellingPrice;

    @ManyToOne
    private ChartOfAccount inventoryAccount;

    private Boolean isActive = true;
}
