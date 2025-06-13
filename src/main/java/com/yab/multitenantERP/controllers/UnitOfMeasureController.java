package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.UnitOfMeasure;
import com.yab.multitenantERP.entity.Warehouse;
import com.yab.multitenantERP.services.UnitOfMeasureService;
import com.yab.multitenantERP.services.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class UnitOfMeasureController {
    UnitOfMeasureService unitOfMeasureService;
    UnitOfMeasureController(UnitOfMeasureService unitOfMeasureService){
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @PostMapping("/unit")
    String addUnit(@RequestBody UnitOfMeasure unitOfMeasure){
        return unitOfMeasureService.addUnitOfMeasure(unitOfMeasure);
    }

    @GetMapping("/unit")
    List<UnitOfMeasure> getAllUnits(){
        return unitOfMeasureService.getAllUnits();
    }
}
