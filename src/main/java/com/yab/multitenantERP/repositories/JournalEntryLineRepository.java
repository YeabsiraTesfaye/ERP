package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.JournalEntryLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {


    @Query("""
    SELECT jel FROM JournalEntryLine jel
    JOIN jel.journalEntry je
    JOIN jel.account coa
    WHERE (:reference IS NULL OR je.reference LIKE %:reference%)
      AND (cast(:fromDate as date) is null OR je.postingDate >= :fromDate)
      AND (cast(:toDate as date) is null OR je.postingDate <= :toDate)
      AND (:chartOfAccountId IS NULL OR coa.id = :chartOfAccountId)
""")
    Page<JournalEntryLine> findFilteredJournalEntryLines(
            @Param("reference") String reference,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("chartOfAccountId") Long chartOfAccountId,
            Pageable pageable
    );

    @Query("""
    SELECT jel FROM JournalEntryLine jel
    JOIN jel.journalEntry je
    JOIN jel.account coa
    WHERE (:fromDate IS NULL OR je.postingDate >= :fromDate)
      AND (:toDate IS NULL OR je.postingDate <= :toDate)
""")
    List<JournalEntryLine> findFilteredJournalEntryLinesForIncomeStatement(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

}
