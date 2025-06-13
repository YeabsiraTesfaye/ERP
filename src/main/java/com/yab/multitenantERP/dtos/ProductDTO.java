package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.ChartOfAccount;
import com.yab.multitenantERP.entity.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String sku;
    private String name;
    private String description;

    @ManyToOne
    private Long uom;

    private Double costPrice;
    private Double sellingPrice;

    @ManyToOne
    private Long inventoryAccount;

    private Boolean isActive = true;
}
