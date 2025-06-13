package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.InventoryAccountMappingDTO;
import com.yab.multitenantERP.dtos.ProductDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.enums.InventoryTransactionType;
import com.yab.multitenantERP.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    ProductRepository productRepository;
    UnitOfMeasurementRepository unitOfMeasurementRepository;
    ChartOfAccountRepository chartOfAccountRepository;
    InventoryAccountMappingRepository inventoryAccountMappingRepository;
    InventoryTransactionRepository inventoryTransactionRepository;

    ProductService(ProductRepository productRepository,
                   UnitOfMeasurementRepository unitOfMeasurementRepository,
                   ChartOfAccountRepository chartOfAccountRepository,
                   InventoryAccountMappingRepository inventoryAccountMappingRepository,
                   InventoryTransactionRepository inventoryTransactionRepository){
        this.productRepository = productRepository;
        this.unitOfMeasurementRepository = unitOfMeasurementRepository;
        this.chartOfAccountRepository = chartOfAccountRepository;
        this.inventoryAccountMappingRepository = inventoryAccountMappingRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public String addProduct(ProductDTO product){
        Product newProduct = new Product();
        Optional<UnitOfMeasure> uom = unitOfMeasurementRepository.findById(product.getUom());
        if(product.getInventoryAccount() != null){
            Optional<ChartOfAccount> coa = chartOfAccountRepository.findById(product.getInventoryAccount());
            coa.ifPresent(newProduct::setInventoryAccount);

        }

        newProduct.setName(product.getName());
        newProduct.setSku(product.getSku());
        newProduct.setDescription(product.getDescription());
        uom.ifPresent(newProduct::setUom);
        newProduct.setCostPrice(product.getCostPrice());
        newProduct.setSellingPrice(product.getSellingPrice());
        newProduct.setIsActive(product.getIsActive());
        productRepository.save(newProduct);
        return "Product registered Successfully";
    }

    public Page<Product> getProducts(String name, String sku,int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.getProducts(name,sku,pageable);
    }

    public Product getProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return product;
    }

    public String mapAccount(InventoryAccountMappingDTO inventoryAccountMappingDTO){
        InventoryAccountMapping inventoryAccountMapping = new InventoryAccountMapping();
        Optional<Product> product = productRepository.findById(inventoryAccountMappingDTO.getProductId());
        Optional<ChartOfAccount> inventoryAccount = chartOfAccountRepository.findById(inventoryAccountMappingDTO.getInventoryAccountId());
        Optional<ChartOfAccount> purchaseAccount = chartOfAccountRepository.findById(inventoryAccountMappingDTO.getPurchaseAccountId());
        Optional<ChartOfAccount> salesAccount = chartOfAccountRepository.findById(inventoryAccountMappingDTO.getSalesAccountId());

        if(product.isPresent() && inventoryAccount.isPresent()){
            product.get().setInventoryAccount(inventoryAccount.get());
            productRepository.save(product.get());
        }

        product.ifPresent(inventoryAccountMapping::setProduct);
        inventoryAccount.ifPresent(inventoryAccountMapping::setInventoryAccount);
        purchaseAccount.ifPresent(inventoryAccountMapping::setPurchaseAccount);
        salesAccount.ifPresent(inventoryAccountMapping::setSalesAccount);

        inventoryAccountMappingRepository.save(inventoryAccountMapping);

        return "Mapping registered Successfully";
    }

    public Page<InventoryAccountMapping> getMappings(String name, String sku,int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return inventoryAccountMappingRepository.getMappings(name,sku,pageable);
    }


    public Page<InventoryTransaction> getTransaction(String reference, InventoryTransactionType type, Long warehouseId, Boolean posted, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        return inventoryTransactionRepository.searchTransactions(reference,type,warehouseId, posted, startDate, endDate,pageable);
    }
}
