package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.PensionTax;
import com.yab.multitenantERP.repositories.PensionTaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PensionTaxService {

    private final PensionTaxRepository pensionTaxRepository;
    PensionTaxService(PensionTaxRepository pensionTaxRepository){
        this.pensionTaxRepository = pensionTaxRepository;
    }
    public PensionTax getPensionTax() {
        List<PensionTax> pensions = pensionTaxRepository.findAll();
        return pensions.isEmpty() ? null : pensions.get(0);
    }
    public PensionTax createOrUpdatePensionTax(PensionTax pensionTax) {
        PensionTax existingPensionTax = getPensionTax();

        if (existingPensionTax == null) {
            // No record exists, so create a new one
            return pensionTaxRepository.save(pensionTax);
        } else {
            // Update the existing record
            existingPensionTax.setCompanyContributionRate(pensionTax.getCompanyContributionRate());
            existingPensionTax.setEmployeeContributionRate(pensionTax.getEmployeeContributionRate());
            return pensionTaxRepository.save(existingPensionTax);
        }
    }
}
