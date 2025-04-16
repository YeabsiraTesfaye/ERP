package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.IncomeTax;
import com.yab.multitenantERP.entity.PensionTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PensionTaxRepository extends JpaRepository<PensionTax, Long> {
}
