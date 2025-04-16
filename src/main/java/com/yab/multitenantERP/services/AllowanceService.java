package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AllowanceService {
    private final AllowanceRepository allowanceRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeAllowanceRepository employeeAllowanceRepository;
    private final PositionAllowanceRepository positionAllowanceRepository;


    public AllowanceService(AllowanceRepository allowanceRepository,
                            EmployeeRepository employeeRepository,
                            PositionRepository positionRepository,
                            EmployeeAllowanceRepository employeeAllowanceRepository,
                            PositionAllowanceRepository positionAllowanceRepository
    ) {
        this.allowanceRepository = allowanceRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeAllowanceRepository = employeeAllowanceRepository;
        this.positionAllowanceRepository = positionAllowanceRepository;
    }

    public List<EmployeeAllowance> addAllowancesToEmployee(Long employeeId, List<EmployeeAllowance> allowanceList) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        for (EmployeeAllowance ea : allowanceList) {
            Long allowanceId = ea.getAllowance().getId(); // assuming the allowance ID is sent in the payload
            Allowance allowance = allowanceRepository.findById(allowanceId)
                    .orElseThrow(() -> new RuntimeException("Allowance not found for ID: " + allowanceId));

            ea.setEmployee(employee);
            ea.setAllowance(allowance);
        }

        return employeeAllowanceRepository.saveAll(allowanceList);
    }



    public List<PositionAllowance> addAllowanceToPosition(Long positionId, List<PositionAllowance> positionAllowances) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        for (PositionAllowance pa : positionAllowances) {
            Long id = pa.getAllowance().getId(); // assuming the allowance ID is sent in the payload
            Allowance allowance = allowanceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Allowance not found"));

            pa.setPosition(position);
            pa.setAllowance(allowance);
        }

        return positionAllowanceRepository.saveAll(positionAllowances);
    }

    public List<Allowance> getAllowancesNotAssignedToPosition(Long positionId) {
        // Get all allowances
        List<Allowance> allAllowances = allowanceRepository.findAll();

        // Get the allowances that the employee already has
        List<PositionAllowance> positionAllowances = positionAllowanceRepository.findByPositionId(positionId);

        // Extract the allowances the employee has into a set of allowance IDs
        Set<Long> positionAllowanceId = positionAllowances.stream()
                .map(employeeAllowance -> employeeAllowance.getAllowance().getId())
                .collect(Collectors.toSet());

        // Filter out the allowances that the employee already has
        return allAllowances.stream()
                .filter(allowance -> !positionAllowanceId.contains(allowance.getId()))
                .collect(Collectors.toList());
    }


    public Allowance createAllowance(Allowance newAllowance) {
        if(!newAllowance.getIsTaxable()){
            newAllowance.setTaxableAmount(null);
        }
        return allowanceRepository.save(newAllowance);
    }

    public List<Allowance> getAllAllowances() {
        return allowanceRepository.findAll();
    }

    public Optional<Allowance> getAllowanceById(Long id) {
        return allowanceRepository.findById(id);
    }

    public List<EmployeeAllowance> getEmployeeAllowances(Long employeeId) {
        return employeeAllowanceRepository.findByEmployeeId(employeeId);
    }

    public List<PositionAllowance> getPositionAllowances(Long positionId) {
        return positionAllowanceRepository.findByPositionId(positionId);
    }

    public Allowance updateAllowance(Long id, Allowance allowance) {
        allowanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allowance not found"));
        allowance.setId(id);
        if(!allowance.getIsTaxable()){
            allowance.setTaxableAmount(null);
        }
        return allowanceRepository.save(allowance);
    }


    public BigDecimal calculateTotalTaxableAllowances(List<EmployeeAllowance> employeeAllowances,
                                                      LocalDate startDate,
                                                      LocalDate endDate) {
        BigDecimal totalTaxable = BigDecimal.ZERO;

        for (EmployeeAllowance ea : employeeAllowances) {
            Allowance allowance = ea.getAllowance();

            if (allowance != null &&
                    allowance.isActive() &&
                    (ea.getStartDate() == null || !ea.getStartDate().isAfter(endDate)) &&
                    (ea.getEndDate() == null || !ea.getEndDate().isBefore(startDate)) &&
                    Boolean.TRUE.equals(allowance.getIsTaxable())) {

                BigDecimal amount = ea.getTaxableAmount() != null
                        ? BigDecimal.valueOf(ea.getTaxableAmount())
                        : BigDecimal.ZERO;

                BigDecimal minNonTaxable = allowance.getTaxableAmount() != null
                        ? BigDecimal.valueOf(allowance.getTaxableAmount())
                        : BigDecimal.ZERO;

                BigDecimal taxableAmount = amount.subtract(minNonTaxable).max(BigDecimal.ZERO);
                totalTaxable = totalTaxable.add(taxableAmount);
            }
        }

        return totalTaxable;
    }
}
