package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.repositories.DepartmentRepository;
import com.yab.multitenantERP.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    PositionRepository positionRepository;
    DepartmentRepository departmentRepository;

    PositionService(PositionRepository positionRepository, DepartmentRepository departmentRepository){
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    public Position registerPosition(Position position){
        Department department = departmentRepository.findById(position.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        position.setDepartment(department);
        return positionRepository.save(position);
    }

    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }

    public List<Position> getPositionsByDepartment(Long id){
        return positionRepository.getPositionByDepartment(id);
    }

    public Optional<Position> getPositionById(Long id){
        return positionRepository.findById(id);
    }

    public Position updatePosition(Long id, Position position){
        positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        position.setId(id);
        return positionRepository.save(position);
    }

}
