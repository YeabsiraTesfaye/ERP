package com.yab.multitenantERP.dtos;

public class ProductStockDTO {
    private Long productId;
    private Double stock;

    public ProductStockDTO(Long productId, Double stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public Double getStock() {
        return stock;
    }
}
