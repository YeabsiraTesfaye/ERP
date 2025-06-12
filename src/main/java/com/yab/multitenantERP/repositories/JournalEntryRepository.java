package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    public List<JournalEntry> findByChartOfAccount_id(Long chartOfAccountsId);

    @Query("""
        SELECT je FROM JournalEntry je
        WHERE (:reference IS NULL OR je.reference LIKE %:reference%)
        AND (:fromDate IS NULL OR je.postingDate >= :fromDate)
        AND (:toDate IS NULL OR je.postingDate <= :toDate)
        AND (:chartOfAccountId IS NULL OR je.chartOfAccount.id = :chartOfAccountId)
    """)
    Page<JournalEntry> findFilteredJournalEntries(
            @Param("reference") String reference,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("chartOfAccountId") Long chartOfAccountId,
            Pageable pageable
    );
}
