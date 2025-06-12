package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Warehouse;
import com.yab.multitenantERP.services.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class WarehouseController {
    WarehouseService warehouseService;
    WarehouseController(WarehouseService warehouseService){
        this.warehouseService = warehouseService;
    }

    @PostMapping("/warehouse")
    String addWarehouse(@RequestBody Warehouse warehouse){
        return warehouseService.addWarehouse(warehouse);
    }

    @GetMapping("/warehouse")
    List<Warehouse> getAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }
}
