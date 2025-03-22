package com.yab.multitenantERP.controllers;

import java.util.List;
import java.util.Optional;

import com.yab.multitenantERP.dtos.EmployeeDTO;
import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeDTO employee) {
        return employeeService.registerUser(employee);
    }

//    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping
    public Page<Employee> getEmployees(
            @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
            @RequestParam(value = "middleName", required = false, defaultValue = "") String middleName,
            @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
            @RequestParam(value = "page", defaultValue = "0") int page,  // Default value
            @RequestParam(value = "size", defaultValue = "10") int size,  // Default value
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,  // Default value
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {

        return employeeService.getFilteredPaginatedEmployees(firstName, lastName, middleName, page, size, sortBy, ascending);
    }
}