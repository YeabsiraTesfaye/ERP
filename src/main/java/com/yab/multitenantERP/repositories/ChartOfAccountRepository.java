package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {
    boolean existsByCode(String code);

    @Query("SELECT c FROM ChartOfAccount c WHERE c.code = ?1")
    Optional<ChartOfAccount> findByCode(String code);

}
