package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Warehouse;
import com.yab.multitenantERP.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {
    WarehouseRepository warehouseRepository;

    WarehouseService(WarehouseRepository warehouseRepository){
        this.warehouseRepository = warehouseRepository;
    }

    public String addWarehouse(Warehouse warehouse){
        warehouseRepository.save(warehouse);
        return "Warehouse registered Successfully";
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
}
