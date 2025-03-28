package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.EmployeeDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    BranchRepository branchRepository;
    PositionRepository positionRepository;
    PositionHistoryRepository positionHistoryRepository;
    DepartmentHistoryRepository departmentHistoryRepository;


    EmployeeService(EmployeeRepository employeeRepository,
                    DepartmentRepository departmentRepository,
                    BranchRepository branchRepository,
                    PositionRepository positionRepository,
                    PositionHistoryRepository positionHistoryRepository,
                    DepartmentHistoryRepository departmentHistoryRepository){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.positionRepository = positionRepository;
        this.positionHistoryRepository = positionHistoryRepository;
        this.departmentHistoryRepository = departmentHistoryRepository;
    }
@Transactional
    public Employee registerUser(EmployeeDTO employee) {
        Employee newEmployee = new Employee();

        Optional<Department> departmentOpt = departmentRepository.findById(employee.getDepartmentId());
        Optional<Branch> branchOpt = branchRepository.findById(employee.getBranchId());
        Optional<Position> positionOpt = positionRepository.findById(employee.getPositionId());

        if (departmentOpt.isPresent() && branchOpt.isPresent() && positionOpt.isPresent()) {
            Department department = departmentOpt.get();
            Branch branch = branchOpt.get();
            Position position = positionOpt.get();

            Integer requiredManPowerDepartment = department.getRequiredManPower(); // May be null
            Integer requiredManPowerPosition = position.getRequiredManPower(); // May be null

            List<Employee> departmentEmployees = department.getEmployees();

            if (requiredManPowerDepartment != null && departmentEmployees.size() >= requiredManPowerDepartment) {
                throw new RuntimeException("Department is full");
            }

            if (requiredManPowerPosition != null && departmentEmployees.size() >= requiredManPowerPosition) {
                throw new RuntimeException("Position is full");
            }

            newEmployee.setFirstName(employee.getFirstName());
            newEmployee.setMiddleName(employee.getMiddleName());
            newEmployee.setLastName(employee.getLastName());
            newEmployee.setGender(employee.getGender());
            newEmployee.setBirthDate(employee.getBirthDate());
            newEmployee.setTinNumber(employee.getTinNumber());
            newEmployee.setEmail(employee.getEmail());
            newEmployee.setPhoneNumber(employee.getPhoneNumber());
            newEmployee.setDepartment(department);
            newEmployee.setPosition(position);
            newEmployee.setBranch(branch);
            newEmployee.setDateOfHire(employee.getDateOfHire());
            newEmployee.setStatus(employee.getStatus());
            newEmployee.setPhoto(employee.getPhoto());

            PositionHistory ph = new PositionHistory();
            ph.setEmployee(newEmployee);
            ph.setPosition(position);
            ph.setEffectiveDate(LocalDate.now());
            positionHistoryRepository.save(ph);

            DepartmentHistory dh = new DepartmentHistory();
            dh.setEmployee(newEmployee);
            dh.setDepartment(department);
            dh.setEffectiveDate(LocalDate.now());
            departmentHistoryRepository.save(dh);

        } else {
            throw new RuntimeException("Department, Branch, or Position not found");
        }

        return employeeRepository.save(newEmployee);
    }

    @Transactional
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee employee){
        employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setId(id);
        return employeeRepository.save(employee);
    }

    public Page<Employee> getFilteredPaginatedEmployees(
            String firstName, String lastName, String middleName,
            int page, int size, String sortBy, boolean ascending) {

        // Creating Pageable object with page number, size, and sorting
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetching the filtered paginated employees
        return employeeRepository.getFilteredEmployees(firstName,middleName, lastName, pageable);
    }

}
