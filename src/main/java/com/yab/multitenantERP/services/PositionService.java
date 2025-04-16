package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.PositionFetchDTO;
import com.yab.multitenantERP.dtos.PositionSaveDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionService {
    PositionRepository positionRepository;
    DepartmentRepository departmentRepository;
    SalaryRepository salaryRepository;
    SalaryHistoryRepository salaryHistoryRepository;
    PositionAllowanceRepository positionAllowanceRepository;
    PositionBenefitRepository positionBenefitRepository;
    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);

    PositionService(PositionRepository positionRepository,
                    DepartmentRepository departmentRepository,
                    SalaryRepository salaryRepository,
                    PositionAllowanceRepository positionAllowanceRepository,
                    PositionBenefitRepository positionBenefitRepository,
                    SalaryHistoryRepository salaryHistoryRepository){
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.salaryRepository = salaryRepository;
        this.salaryHistoryRepository = salaryHistoryRepository;
        this.positionAllowanceRepository = positionAllowanceRepository;
        this.positionBenefitRepository = positionBenefitRepository;
    }

    public Position registerPosition(Position position) {
        Department department = departmentRepository.findById(position.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Create Position instance and set department
        Position newPosition = new Position();
        newPosition.setName(position.getName());
        newPosition.setDepartment(department);
        newPosition.setRequiredManPower(position.getRequiredManPower());

        Position savedPosition = positionRepository.save(newPosition);

        Salary newSalary = new Salary();
        newSalary.setAmount(position.getSalary().getAmount());
        newSalary.setEffectiveDate(position.getSalary().getEffectiveDate());
        newSalary.setPosition(position.getSalary().getPosition());
        Salary savedSalary = salaryRepository.save(newSalary);

        savedPosition.setSalary(savedSalary);
        savedPosition.setBenefits(position.getBenefits());
        positionRepository.save(savedPosition);

        return savedPosition;
    }


    public List<Position> getAllPositions(){
        return positionRepository.findAll();
    }

    public List<Position> getPositionsByDepartment(Long id){
        return positionRepository.findByDepartmentId(id);
    }

    public Optional<Position> getPositionById(Long id){
        return positionRepository.findById(id);
    }
    public PositionFetchDTO fetchPositionAllowanceDTO(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        List<PositionAllowance> positionAllowances = positionAllowanceRepository.findByPositionId(positionId);
        List<PositionBenefit> positionBenefits = positionBenefitRepository.findByPositionId(positionId);

        PositionFetchDTO dto = new PositionFetchDTO();
        dto.setPosition(position);
        dto.setPositionBenefits(positionBenefits);
        dto.setPositionAllowances(positionAllowances);

        return dto;
    }


    public Position updatePosition(Long id, Position position) {
        Position oldPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        if (position.getSalary() != null) {
            Salary newSalaryInput = position.getSalary();
            Salary oldSalary = oldPosition.getSalary();
            if (oldSalary != null) {
                // Check only if the amount has changed
                boolean amountChanged = !newSalaryInput.getAmount().equals(oldSalary.getAmount());

                if (amountChanged) {
                    Salary newSalary = new Salary();
                    newSalary.setAmount(newSalaryInput.getAmount());
                    newSalary.setEffectiveDate(newSalaryInput.getEffectiveDate());
                    newSalary.setPosition(oldPosition);
                    Salary savedSalary = salaryRepository.save(newSalary);
                    position.setSalary(savedSalary);
                } else {
                    // Keep the existing salary if the amount is the same
                    position.setSalary(oldSalary);
                }
            } else {
                // No old salary exists, so create a new one
                Salary newSalary = new Salary();
                newSalary.setAmount(newSalaryInput.getAmount());
                newSalary.setEffectiveDate(newSalaryInput.getEffectiveDate());
                newSalary.setPosition(oldPosition);
                Salary savedSalary = salaryRepository.save(newSalary);
                position.setSalary(savedSalary);
            }

        }

        position.setId(id);
        return positionRepository.save(position);
    }


}
