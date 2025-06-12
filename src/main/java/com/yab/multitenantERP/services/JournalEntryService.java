package com.yab.multitenantERP.services;

import com.yab.multitenantERP.dtos.AccountingReportDTO;
import com.yab.multitenantERP.dtos.JournalEntryDTO;
import com.yab.multitenantERP.dtos.JournalEntryLineDTO;
import com.yab.multitenantERP.dtos.TrialBalanceDTO;
import com.yab.multitenantERP.entity.ChartOfAccount;
import com.yab.multitenantERP.entity.JournalEntry;
import com.yab.multitenantERP.entity.JournalEntryLine;
import com.yab.multitenantERP.enums.AccountType;
import com.yab.multitenantERP.enums.EntryStatus;
import com.yab.multitenantERP.exceptions.NotFoundException;
import com.yab.multitenantERP.repositories.ChartOfAccountRepository;
import com.yab.multitenantERP.repositories.JournalEntryLineRepository;
import com.yab.multitenantERP.repositories.JournalEntryRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class JournalEntryService {
    private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final ChartOfAccountRepository chartOfAccountRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               ChartOfAccountRepository chartOfAccountRepository,
                               JournalEntryLineRepository journalEntryLineRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.chartOfAccountRepository = chartOfAccountRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
    }

    public String createJournalEntry(List<JournalEntryDTO> journalEntryDTOS) {
        try {
            for (JournalEntryDTO dto : journalEntryDTOS) {
                JournalEntry entry = new JournalEntry();
                entry.setReference(dto.getReference());
                entry.setDescription(dto.getDescription());
                entry.setPostingDate(dto.getDate());
                entry.setStatus(EntryStatus.DRAFT);

                List<JournalEntryLine> lines = dto.getLines().stream().map(lineDto -> {
                    JournalEntryLine line = new JournalEntryLine();
                    line.setDebit(lineDto.getDebit());
                    line.setCredit(lineDto.getCredit());
                    line.setJournalEntry(entry);

                    Optional<ChartOfAccount> chartOfAccount = chartOfAccountRepository.findByCode(lineDto.getAccountCode());
                    if (chartOfAccount.isEmpty()) {
                        throw new NotFoundException("Chart of account not found with code");
                    }

                    line.setAccount(chartOfAccount.get());
                    return line;
                }).toList();

                entry.setLines(lines);
                journalEntryRepository.save(entry);
            }

            return "Journal entry is recorded successfully";

        } catch (Exception e) {
            throw new NotFoundException("Chart of account Does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Optional<JournalEntry> findById(Long id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<JournalEntry> findByCode(Long companyId, Long chartOfAccountId) {
        return journalEntryRepository.findByChartOfAccount_id(companyId);
    }

    @Transactional(readOnly = true)
    public Page<JournalEntry> findFilteredJournalEntries(String reference, LocalDate fromDate, LocalDate toDate, Long accountId, Pageable pageable) {
        Page<JournalEntry> page = journalEntryRepository.findFilteredJournalEntries(reference, fromDate, toDate, accountId, pageable);
        return page; // map each JournalEntry to JournalEntryDTO
    }

    private JournalEntryDTO mapToDTO(JournalEntry je) {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setReference(je.getReference());
        dto.setDescription(je.getDescription());
        dto.setDate(je.getPostingDate());
        dto.setId(je.getId());

        if (je.getLines() != null) {
            List<JournalEntryLineDTO> lines = je.getLines().stream().map(line -> {
                JournalEntryLineDTO lineDTO = new JournalEntryLineDTO();
                lineDTO.setAccountCode(line.getAccount().getCode());
                lineDTO.setDebit(line.getDebit());
                lineDTO.setCredit(line.getCredit());
                return lineDTO;
            }).toList();
            dto.setLines(lines);
        }

        return dto;
    }


    @Transactional(readOnly = true)
    public Page<JournalEntryLine> findFilteredJournalEntryLines(String reference, LocalDate fromDate, LocalDate toDate, Long accountId, Pageable pageable) {
        return journalEntryLineRepository.findFilteredJournalEntryLines(reference,fromDate,toDate,accountId,pageable);
    }

    public String migrateBalance(JournalEntryDTO dto) {

        long count = journalEntryRepository.count();
        if(count > 0){
            throw new NotFoundException("Can not migrate initial balance. Entry already exists");
        }
            JournalEntry entry = new JournalEntry();
            entry.setReference(dto.getReference());
            entry.setDescription(dto.getDescription());
            entry.setPostingDate(dto.getDate());
            entry.setStatus(EntryStatus.DRAFT); // Or whatever default


            List<JournalEntryLine> lines = dto.getLines().stream().map(lineDto -> {
                JournalEntryLine line = new JournalEntryLine();
//                line.setAccountCode(lineDto.getAccountCode());
                line.setDebit(lineDto.getDebit());
                line.setCredit(lineDto.getCredit());
                line.setJournalEntry(entry); // Important!
                Optional<ChartOfAccount> chartOfAccount = chartOfAccountRepository.findByCode(lineDto.getAccountCode());
                if(chartOfAccount.isPresent()){
                    entry.setChartOfAccount(chartOfAccount.get());
                    line.setAccount(chartOfAccount.get());
                }
                return line;
            }).toList();

            entry.setLines(lines);
            journalEntryRepository.save(entry);

        return "Journal entry is recorded successfully";
    }

    public List<AccountingReportDTO> generateIncomeStatement(Long companyId, LocalDate fromDate, LocalDate toDate) {
        List<JournalEntryLine> journalEntryLines = journalEntryLineRepository.findFilteredJournalEntryLinesForIncomeStatement(fromDate, toDate);

        Map<String, AccountingReportDTO> expenseMap = new HashMap<>();
        Map<String, AccountingReportDTO> revenueMap = new HashMap<>();

        for (JournalEntryLine j : journalEntryLines) {
            if (j.getAccount().getType() == AccountType.EXPENSE) {
                String accountName = j.getAccount().getName();
                BigDecimal netAmount = j.getDebit().subtract(j.getCredit()); // Debit - Credit for EXPENSE

                if (expenseMap.containsKey(accountName)) {
                    AccountingReportDTO existing = expenseMap.get(accountName);
                    existing.setAmount(existing.getAmount().add(netAmount));
                } else {
                    AccountingReportDTO accountingReportDTO = new AccountingReportDTO();
                    accountingReportDTO.setName(accountName);
                    accountingReportDTO.setType(AccountType.EXPENSE);
                    accountingReportDTO.setGlCode(j.getAccount().getCode());
                    accountingReportDTO.setAmount(netAmount);

                    expenseMap.put(accountName, accountingReportDTO);
                }
            }

            if (j.getAccount().getType() == AccountType.REVENUE) {
                String accountName = j.getAccount().getName();
                BigDecimal netAmount = j.getCredit().subtract(j.getDebit()); // Credit - Debit for REVENUE

                if (revenueMap.containsKey(accountName)) {
                    AccountingReportDTO existing = revenueMap.get(accountName);
                    existing.setAmount(existing.getAmount().add(netAmount));
                } else {
                    AccountingReportDTO accountingReportDTO = new AccountingReportDTO();
                    accountingReportDTO.setName(accountName);
                    accountingReportDTO.setType(AccountType.REVENUE);
                    accountingReportDTO.setGlCode(j.getAccount().getCode());
                    accountingReportDTO.setAmount(netAmount);

                    revenueMap.put(accountName, accountingReportDTO);
                }
            }
        }

        List<AccountingReportDTO> incomes = new ArrayList<>(revenueMap.values());
        List<AccountingReportDTO> expenses = new ArrayList<>(expenseMap.values());
        List<AccountingReportDTO> results = new ArrayList<>();

        results.addAll(incomes);
        results.addAll(expenses);
        return results;
    }

    public List<AccountingReportDTO> generateBalanceSheet(Long companyId, LocalDate date){
        List<AccountingReportDTO> accountingReportDTOS = generateIncomeStatement(companyId,date,date);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        for(AccountingReportDTO accountingReportDTO: accountingReportDTOS){
            if(accountingReportDTO.getType() == AccountType.REVENUE){
                totalRevenue=accountingReportDTO.getAmount().add(totalRevenue);
            }else{
                totalExpense=accountingReportDTO.getAmount().add(totalExpense);
            }
        }
        List<JournalEntryLine> journalEntryLines = journalEntryLineRepository.findFilteredJournalEntryLinesForIncomeStatement(date, date);

        Map<String, AccountingReportDTO> assetMap = new HashMap<>();
        Map<String, AccountingReportDTO> liabilityAndEquityMap = new HashMap<>();

        for (JournalEntryLine j : journalEntryLines) {
            if (j.getAccount().getType() == AccountType.ASSET) {
                String accountName = j.getAccount().getName();

                BigDecimal netAmount = j.getDebit().subtract(j.getCredit()); // Debit - Credit

                if (assetMap.containsKey(accountName)) {
                    AccountingReportDTO existing = assetMap.get(accountName);
                    existing.setAmount(existing.getAmount().add(netAmount));
                } else {
                    AccountingReportDTO accountingReportDTO = new AccountingReportDTO();
                    accountingReportDTO.setName(accountName);
                    accountingReportDTO.setType(AccountType.ASSET);
                    accountingReportDTO.setGlCode(j.getAccount().getCode());
                    accountingReportDTO.setAmount(netAmount);

                    assetMap.put(accountName, accountingReportDTO);
                }
            }

            if (j.getAccount().getType() == AccountType.LIABILITY || j.getAccount().getType() == AccountType.EQUITY) {
                String accountName = j.getAccount().getName();
                BigDecimal netAmount = j.getCredit().subtract(j.getDebit()); // Debit - Credit

                if (liabilityAndEquityMap.containsKey(accountName)) {
                    AccountingReportDTO existing = liabilityAndEquityMap.get(accountName);
                    existing.setAmount(existing.getAmount().add(netAmount));
                } else {
                    AccountingReportDTO accountingReportDTO = new AccountingReportDTO();
                    accountingReportDTO.setName(accountName);
                    accountingReportDTO.setType(j.getAccount().getType());
                    accountingReportDTO.setGlCode(j.getAccount().getCode());
                    accountingReportDTO.setAmount(netAmount);

                    liabilityAndEquityMap.put(accountName, accountingReportDTO);
                }
            }
        }

        List<AccountingReportDTO> liabilityAndEquity = new ArrayList<>(liabilityAndEquityMap.values());
        List<AccountingReportDTO> asset = new ArrayList<>(assetMap.values());
        List<AccountingReportDTO> results = new ArrayList<>();

        results.addAll(liabilityAndEquity);
        results.addAll(asset);
        AccountingReportDTO netProfit = new AccountingReportDTO();
        netProfit.setAmount(totalRevenue.subtract(totalExpense));
        netProfit.setType(AccountType.EQUITY);
        netProfit.setName("Net Profit");
        results.add(netProfit);
        return results;
    }

    public List<TrialBalanceDTO> generateTrialBalance(Long companyId, LocalDate fromDate, LocalDate toDate) {
        List<JournalEntryLine> journalEntryLines = journalEntryLineRepository
                .findFilteredJournalEntryLinesForIncomeStatement(fromDate, toDate);

        Map<String, AccountingReportDTO> map = new HashMap<>();

        for (JournalEntryLine j : journalEntryLines) {
            String accountName = j.getAccount().getName();
            BigDecimal netAmount = j.getDebit().subtract(j.getCredit());

            if (map.containsKey(accountName)) {
                AccountingReportDTO existing = map.get(accountName);
                existing.setAmount(existing.getAmount().add(netAmount));
            } else {
                AccountingReportDTO accountingReportDTO = new AccountingReportDTO();
                accountingReportDTO.setName(accountName);
                accountingReportDTO.setType(j.getAccount().getType());
                accountingReportDTO.setGlCode(j.getAccount().getCode());
                accountingReportDTO.setAmount(netAmount);

                map.put(accountName, accountingReportDTO);
            }
        }

        ArrayList<TrialBalanceDTO> trialBalanceDTOS = new ArrayList<>();
        for(AccountingReportDTO ard : map.values()){
            TrialBalanceDTO trialBalanceDTO = new TrialBalanceDTO();

            if(ard.getAmount().doubleValue() < 0){
                trialBalanceDTO.setCredit(ard.getAmount().abs());
            }else {
                trialBalanceDTO.setDebit(ard.getAmount());
            }
            trialBalanceDTO.setName(ard.getName());
            trialBalanceDTO.setGlCode(ard.getGlCode());
            trialBalanceDTO.setType(ard.getType());
            trialBalanceDTOS.add(trialBalanceDTO);
        }

        return new ArrayList<>(trialBalanceDTOS);
    }




}