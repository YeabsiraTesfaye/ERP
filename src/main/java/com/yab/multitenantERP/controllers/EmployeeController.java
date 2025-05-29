package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.EmployeeFetchDTO;
import com.yab.multitenantERP.dtos.ManagerAndSupervisorDTO;
import com.yab.multitenantERP.dtos.PositionFetchDTO;
import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.registerUser(employee);
    }

    @GetMapping("/{id}")
    public EmployeeFetchDTO getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping
    public Page<EmployeeFetchDTO> getEmployees(
            @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
            @RequestParam(value = "middleName", required = false, defaultValue = "") String middleName,
            @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
            @RequestParam(value = "page", defaultValue = "0") int page,  // Default value
            @RequestParam(value = "size", defaultValue = "10") int size,  // Default value
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,  // Default value
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {

        return employeeService.getFilteredPaginatedEmployees(firstName, lastName, middleName, page, size, sortBy, ascending);
    }

    @PutMapping("/updateManagerSupervisor/{employeeId}")
    public Employee updateManagerAndSupervisor(@PathVariable Long employeeId, @RequestBody ManagerAndSupervisorDTO managerAndSupervisorDTO){
        return  employeeService.assignManagerAndSupervisor(employeeId, managerAndSupervisorDTO);
    }

    @GetMapping("/supervisor/{id}")
    public List<Employee> getEmployeeBySupervisor(@RequestParam Long supervisorId){
        return employeeService.getEmployeesBySupervisor(supervisorId);
    }


    @GetMapping("/manager/{id}")
    public List<Employee> getEmployeeByManager(@RequestParam Long supervisorId){
        return employeeService.getEmployeesByManager(supervisorId);
    }

    @GetMapping("/branch/{id}")
    public List<Employee> getEmployeeByBranch(@RequestParam Long branchId){
        return employeeService.getEmployeesByBranch(branchId);
    }

    @PostMapping("/levelAndDepartment/{departmentId}")
    public List<Employee> getEmployeeByLevelAndDepartment(@PathVariable Long departmentId, @RequestBody Long level){
        return employeeService.getEmployeesByDepartmentAndLevel(level, departmentId);
    }
}