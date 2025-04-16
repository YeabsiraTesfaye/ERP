package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.IncomeTax;
import com.yab.multitenantERP.repositories.IncomeTaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeTaxService {

    private final IncomeTaxRepository incomeTaxRepository;

    public IncomeTax createIncomeTax(IncomeTax incomeTax) {
        return incomeTaxRepository.save(incomeTax);
    }

    public IncomeTax getIncomeTaxByLevel(String level) {
        return incomeTaxRepository.findByLevel(level)
                .orElseThrow(() -> new RuntimeException("Income tax rule not found for level: " + level));
    }

    public IncomeTax getIncomeTaxById(Long id) {
        return incomeTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income tax rule not found for id: " + id));
    }

    public IncomeTax updateIncomeTax(Long id, IncomeTax updatedIncomeTax) {
        IncomeTax existingTax = getIncomeTaxById(id);
        existingTax.setMinimum(updatedIncomeTax.getMinimum());
        existingTax.setMaximum(updatedIncomeTax.getMaximum());
        existingTax.setRate(updatedIncomeTax.getRate());
        existingTax.setLevel(updatedIncomeTax.getLevel());
        existingTax.setDeduction(updatedIncomeTax.getDeduction());
        return incomeTaxRepository.save(existingTax);
    }

    public BigDecimal calculateTax(BigDecimal income, String level) {


        // Calculate tax: for example, tax = income * rate
        return income.multiply(BigDecimal.valueOf(1));
    }

    // New method to get all income taxes
    public List<IncomeTax> getAllIncomeTaxes() {
        return incomeTaxRepository.findAll();
    }

    public IncomeTax getIncomeTaxBySalary(BigDecimal salary){
        return incomeTaxRepository.findTaxBracket(salary.doubleValue());
    }
}
