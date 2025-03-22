package com.yab.multitenantERP.controllers;

import java.util.List;
import java.util.Optional;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.services.DepartmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
class DepartmentController {

    private final DepartmentService departmentService;

    DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @PostMapping
    public Department createDepartment(@RequestHeader("X-Company-Schema") String companyName, @RequestBody Department department) {
        return departmentService.registerDepartment(department);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartmentes();
    }

    @GetMapping("/{id}")
    public Optional<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }
}