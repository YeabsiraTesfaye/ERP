package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.IncomeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface IncomeTaxRepository extends JpaRepository<IncomeTax, Long> {
    @Query("SELECT i FROM IncomeTax i WHERE :salary >= i.minimum AND (:salary <= i.maximum OR i.maximum IS NULL)")
    IncomeTax findTaxBracket(@Param("salary") Double salary);

    Optional<IncomeTax> findByLevel(String level);

    Optional<IncomeTax> findByMinimumLessThanEqualAndMaximumGreaterThanEqual(Double minimum, Double maximum);
}
