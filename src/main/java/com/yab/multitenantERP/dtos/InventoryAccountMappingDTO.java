package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.ChartOfAccount;
import com.yab.multitenantERP.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class InventoryAccountMappingDTO {
    @ManyToOne
    private Long productId;

    @ManyToOne
    private Long purchaseAccountId;

    @ManyToOne
    private Long salesAccountId;

    @ManyToOne
    private Long inventoryAccountId;
}
