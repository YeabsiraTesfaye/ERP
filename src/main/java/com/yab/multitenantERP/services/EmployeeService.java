package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.EmployeeFetchDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;
    BranchRepository branchRepository;
    PositionRepository positionRepository;
    DepartmentRepository departmentRepository;
    EmployeeAllowanceRepository employeeAllowanceRepository;
    PositionAllowanceRepository positionAllowanceRepository;
    EmployeeBenefitRepository employeeBenefitRepository;
    PositionBenefitRepository positionBenefitRepository;


    EmployeeService(EmployeeRepository employeeRepository,
                    BranchRepository branchRepository,
                    PositionRepository positionRepository,
                    PositionHistoryRepository positionHistoryRepository,
                    EmployeeAllowanceRepository employeeAllowanceRepository,
    EmployeeBenefitRepository employeeBenefitRepository,
    PositionBenefitRepository positionBenefitRepository,
    PositionAllowanceRepository positionAllowanceRepository,
                    DepartmentRepository departmentRepository){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.positionRepository = positionRepository;
        this.employeeAllowanceRepository = employeeAllowanceRepository;
        this.positionAllowanceRepository = positionAllowanceRepository;
        this.employeeBenefitRepository = employeeBenefitRepository;
        this.positionBenefitRepository = positionBenefitRepository;

    }
@Transactional
public Employee registerUser(Employee employee) {

    Optional<Department> departmentOpt = departmentRepository.findById(employee.getDepartment().getId());
    Optional<Branch> branchOpt = branchRepository.findById(employee.getBranch().getId());
    Optional<Position> positionOpt = positionRepository.findById(employee.getPosition().getId());

    if (departmentOpt.isPresent() && branchOpt.isPresent() && positionOpt.isPresent()) {
        Department department = departmentOpt.get();
        Branch branch = branchOpt.get();
        Position position = positionOpt.get();

        employee.setDepartment(department);
        employee.setBranch(branch);
        employee.setPosition(position);

        Integer requiredManPowerDepartment = department.getRequiredManPower();
        Integer requiredManPowerPosition = position.getRequiredManPower();

        List<Employee> departmentEmployees = department.getEmployees();
        if (requiredManPowerDepartment != null && departmentEmployees.size() >= requiredManPowerDepartment) {
            throw new RuntimeException("Department is full");
        }

        if (requiredManPowerPosition != null && departmentEmployees.size() >= requiredManPowerPosition) {
            throw new RuntimeException("Position is full");
        }
    } else {
        throw new RuntimeException("Department, Branch, or Position not found");
    }

    return employeeRepository.save(employee);
}
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public EmployeeFetchDTO getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<EmployeeAllowance> employeeAllowances = employeeAllowanceRepository.findByEmployeeId(id);
        List<PositionAllowance> positionAllowances = positionAllowanceRepository.findByPositionId(employee.getPosition().getId());
        List<EmployeeBenefit> employeeBenefits = employeeBenefitRepository.findByBenefitId(employee.getId());
        List<PositionBenefit> positionBenefits = positionBenefitRepository.findByPositionId(employee.getPosition().getId());

        Map<String, EmployeeAllowance> allowanceMap = new HashMap<>();

        for (EmployeeAllowance ea : employeeAllowances) {
            String type = ea.getAllowance().getAllowanceType();
            allowanceMap.put(type, ea);
        }

        for (PositionAllowance pa : positionAllowances) {
            String type = pa.getAllowance().getAllowanceType();
            if (!allowanceMap.containsKey(type)) {
                EmployeeAllowance ea = new EmployeeAllowance();
                ea.setAllowance(pa.getAllowance());
                ea.setAmount(pa.getAmount());
                allowanceMap.put(type, ea);
            }
        }
        List<EmployeeAllowance> finalAllowances = new ArrayList<>(allowanceMap.values());

//        benefits
        Map<String, EmployeeBenefit> benefitMap = new HashMap<>();
        for (EmployeeBenefit eb : employeeBenefits) {
            String type = eb.getBenefit().getBenefitType();
            benefitMap.put(type, eb);
        }
        for (PositionBenefit pb : positionBenefits) {
            String type = pb.getBenefit().getBenefitType();
            if (!benefitMap.containsKey(type)) {
                EmployeeBenefit eb = new EmployeeBenefit();
                eb.setBenefit(pb.getBenefit());
                eb.setStartDate(pb.getStartDate());
                eb.setEndDate(pb.getEndDate());
                benefitMap.put(type, eb);
            }
        }
        List<EmployeeBenefit> finalBenefits = new ArrayList<>(benefitMap.values());
        EmployeeFetchDTO employeeFetchDTO = new EmployeeFetchDTO();
        employeeFetchDTO.setEmployee(employee);
        employeeFetchDTO.setEmployeeAllowances(finalAllowances);
        employeeFetchDTO.setEmployeeBenefits(finalBenefits);
        return employeeFetchDTO;


    }

    public Employee updateEmployee(Long id, Employee employee){
        employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setId(id);
        return employeeRepository.save(employee);
    }

    public Page<EmployeeFetchDTO> getFilteredPaginatedEmployees(
            String firstName, String lastName, String middleName,
            int page, int size, String sortBy, boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch the page of employees from repository
        Page<Employee> employeePage = employeeRepository.getFilteredEmployees(firstName, middleName, lastName, pageable);

        // Convert to DTOs
        List<EmployeeFetchDTO> dtoList = employeePage.stream().map(e -> {
            EmployeeFetchDTO dto = new EmployeeFetchDTO();
            dto.setEmployee(e);
            List<EmployeeAllowance> employeeAllowances = employeeAllowanceRepository.findByEmployeeId(e.getId());
            dto.setEmployeeAllowances(employeeAllowances);
            return dto;
        }).toList();

        // Return as Page<EmployeeFetchDTO>
        return new PageImpl<>(dtoList, pageable, employeePage.getTotalElements());
    }


}
