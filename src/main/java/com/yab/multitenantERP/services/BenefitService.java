package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenefitService {
    BenefitRepository benefitRepository;
    EmployeeRepository employeeRepository;
    PositionRepository positionRepository;
    EmployeeBenefitRepository employeeBenefitRepository;
    PositionBenefitRepository positionBenefitRepository;

    BenefitService(BenefitRepository benefitRepository,
                   EmployeeRepository employeeRepository,
                   PositionRepository positionRepository,
                   EmployeeBenefitRepository employeeBenefitRepository,
                   PositionBenefitRepository positionBenefitRepository
    ){
        this.benefitRepository = benefitRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeBenefitRepository = employeeBenefitRepository;
        this.positionBenefitRepository = positionBenefitRepository;
    }

    public List<EmployeeBenefit> addBenefitToEmployee(Long employeeId, List<EmployeeBenefit> benefits) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        for (EmployeeBenefit eb : benefits) {
            Long benefitId = eb.getBenefit().getId(); // assuming the allowance ID is sent in the payload
            Benefit benefit = benefitRepository.findById(benefitId)
                    .orElseThrow(() -> new RuntimeException("Allowance not found"));

            eb.setEmployee(employee);
            eb.setBenefit(benefit);
        }

        return employeeBenefitRepository.saveAll(benefits);
    }

    public List<PositionBenefit> addBenefitToPosition(Long positionId, List<PositionBenefit> positionBenefit) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        for (PositionBenefit pb : positionBenefit) {
            Long id = pb.getBenefit().getId();
            Benefit benefit = benefitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Benefit not found"));

            pb.setPosition(position);
            pb.setBenefit(benefit);
        }

        return positionBenefitRepository.saveAll(positionBenefit);
    }



    public Benefit createBenefit( Benefit newBenefit) {
        return benefitRepository.save(newBenefit);
    }

    public List<Benefit> getAllBenefits(){
        return benefitRepository.findAll();
    }

    public Optional<Benefit> getBenefitById(Long id){
        return benefitRepository.findById(id);
    }

    public List<Benefit> getBenefitByEmployeeId(Long id){
        return benefitRepository.findByEmployeeId(id);
    }
    public List<Benefit> getBenefitByPositionId(Long id){
        return benefitRepository.findByPositionId(id);
    }

    public Benefit updateBenefit(Long id, Benefit benefit){
        benefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefit not found"));
        benefit.setId(id);
        return benefitRepository.save(benefit);
    }

}
