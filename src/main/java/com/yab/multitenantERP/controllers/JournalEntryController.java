package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.JournalEntryDTO;
import com.yab.multitenantERP.entity.JournalEntry;
import com.yab.multitenantERP.entity.JournalEntryLine;
import com.yab.multitenantERP.services.JournalEntryService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance")
public class JournalEntryController {
    private final JournalEntryService journalEntryService;
    private static final Logger log = LogManager.getLogger(JournalEntryController.class);
    @PostMapping("/journalEntry")
    public ResponseEntity<String> register(@RequestBody List<JournalEntryDTO> journalEntryDTO){
        return new ResponseEntity<>(this.journalEntryService.createJournalEntry(journalEntryDTO), HttpStatus.OK);
    }
    @PostMapping("/migrateBalance")
    public ResponseEntity<String> migrate(@RequestBody JournalEntryDTO journalEntryDTO){
        String result = journalEntryService.migrateBalance(journalEntryDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getJournalEntry/{code}")
    public ResponseEntity<List<JournalEntry>> getChartOfAccounts(@RequestHeader(value = "User-Id") String userId, @RequestHeader(value = "Company-Id") Long companyId, @PathVariable Long code){
        return new ResponseEntity<>(this.journalEntryService.findByCode(companyId, code), HttpStatus.OK);
    }

    @GetMapping("/journalEntries")
    public Page<JournalEntry> getJournalEntries(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long chartOfAccountId,
            Pageable pageable
    ) {
        return journalEntryService.findFilteredJournalEntries(reference,fromDate, toDate, chartOfAccountId, pageable);
    }

    @GetMapping("/journalEntryLines")
    public Page<JournalEntryLine> getJournalEntryLines(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long chartOfAccountId,
            Pageable pageable
    ) {
        return journalEntryService.findFilteredJournalEntryLines(reference,fromDate, toDate, chartOfAccountId, pageable);
    }
}
