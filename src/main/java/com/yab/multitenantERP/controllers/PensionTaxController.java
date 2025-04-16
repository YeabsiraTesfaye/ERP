package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.PensionTax;
import com.yab.multitenantERP.services.HolidayService;
import com.yab.multitenantERP.services.PensionTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pension-tax")
public class PensionTaxController {
    private final PensionTaxService pensionTaxService;
    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    public PensionTaxController(PensionTaxService pensionTaxService) {
        this.pensionTaxService = pensionTaxService;
    }
    @GetMapping
    public ResponseEntity<PensionTax> getPensionTax() {
        PensionTax pensionTax = pensionTaxService.getPensionTax();
        if (pensionTax == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pensionTax);
    }
    @PutMapping
    public ResponseEntity<PensionTax> createOrUpdatePensionTax(@RequestBody PensionTax updatedPensionTax) {
        System.out.println("here");
        PensionTax pensionTax = pensionTaxService.createOrUpdatePensionTax(updatedPensionTax);
        return ResponseEntity.ok(pensionTax);
    }


}
