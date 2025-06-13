package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.UnitOfMeasure;
import com.yab.multitenantERP.entity.Warehouse;
import com.yab.multitenantERP.repositories.UnitOfMeasurementRepository;
import com.yab.multitenantERP.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitOfMeasureService {
    UnitOfMeasurementRepository unitOfMeasurementRepository;

    UnitOfMeasureService(UnitOfMeasurementRepository unitOfMeasurementRepository){
        this.unitOfMeasurementRepository = unitOfMeasurementRepository;
    }

    public String addUnitOfMeasure(UnitOfMeasure unitOfMeasure){
        unitOfMeasurementRepository.save(unitOfMeasure);
        return "Unit registered Successfully";
    }

    public List<UnitOfMeasure> getAllUnits() {
        return unitOfMeasurementRepository.findAll();
    }
}
