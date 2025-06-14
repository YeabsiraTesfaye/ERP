package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @Transient
    private Double currentStock;

}
