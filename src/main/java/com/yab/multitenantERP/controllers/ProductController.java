package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.InventoryAccountMappingDTO;
import com.yab.multitenantERP.dtos.ProductDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.enums.InventoryTransactionType;
import com.yab.multitenantERP.exceptions.NotFoundException;
import com.yab.multitenantERP.services.ProductService;
import com.yab.multitenantERP.services.UnitOfMeasureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class ProductController {
    ProductService productService;
    ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/product")
    String addProduct(@RequestBody ProductDTO product){
        return productService.addProduct(product);
    }

    @GetMapping("/product")
    Page<Product> getAllProducts( @RequestParam(value = "name", required = false, defaultValue = "") String name,
                               @RequestParam(value = "sku", required = false, defaultValue = "") String sku,
                               @RequestParam(value = "page", defaultValue = "0") int page,  // Default value
                               @RequestParam(value = "size", defaultValue = "10") int size,  // Default value
                               @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,  // Default value
                               @RequestParam(value = "ascending", defaultValue = "true") boolean ascending){
        return productService.getProducts(name,sku,page,size,sortBy,ascending);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PostMapping("/accountMapping")
    String mapAccount(@RequestBody InventoryAccountMappingDTO inventoryAccountMappingDTO){
        return productService.mapAccount(inventoryAccountMappingDTO);
    }

    @GetMapping("/accountMapping")
    Page<InventoryAccountMapping> getMappings( @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                  @RequestParam(value = "sku", required = false, defaultValue = "") String sku,
                                  @RequestParam(value = "page", defaultValue = "0") int page,  // Default value
                                  @RequestParam(value = "size", defaultValue = "10") int size,  // Default value
                                  @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,  // Default value
                                  @RequestParam(value = "ascending", defaultValue = "true") boolean ascending){
        return productService.getMappings(name,sku,page,size,sortBy,ascending);
    }

    @GetMapping("/transactions")
    public Page<InventoryTransaction> getTransactions(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) InventoryTransactionType type,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Boolean posted,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return productService.getTransaction(reference, type, warehouseId, posted, startDate, endDate, pageable);
    }

    @PostMapping("/transactions")
    String addTransaction(@RequestBody ProductDTO product){
        return productService.addProduct(product);
    }

}
