package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    public Department registerDepartment(Department Department){
        return departmentRepository.save(Department);
    }

    public List<Department> getAllDepartmentes(){
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id){
        return departmentRepository.findById(id);
    }

    public Department updateDepartment(Long id, Department department){
        departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setId(id);
        return departmentRepository.save(department);
    }

}
