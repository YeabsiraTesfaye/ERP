package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeductionService {
    private final EmployeeDeductionRepository employeeDeductionRepository;
    private final EmployeeRepository employeeRepository;


    public DeductionService(EmployeeDeductionRepository employeeDeductionRepository,
                            EmployeeRepository employeeRepository
    ) {
        this.employeeDeductionRepository = employeeDeductionRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Deduction> addDeductionToEmployee(Long employeeId, List<Deduction> deductionsList) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
for(Deduction d: deductionsList){
    d.setEmployee(employee);
}


        return employeeDeductionRepository.saveAll(deductionsList);
    }

    public Optional<Deduction> getDeductionById(Long id) {
        return employeeDeductionRepository.findById(id);
    }

    public List<Deduction> getEmployeeDeductions(Long employeeId) {
        return employeeDeductionRepository.findByEmployeeId(employeeId);
    }

    public List<Deduction> getAllDeductions(){
        return employeeDeductionRepository.findAll();
    }

    public Deduction updateDeduction(Long id, Deduction deduction) {
        Deduction oldDeduction = employeeDeductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allowance not found"));

        deduction.setId(oldDeduction.getId());
        deduction.setEmployee(oldDeduction.getEmployee());
        return employeeDeductionRepository.save(deduction);
    }
}
