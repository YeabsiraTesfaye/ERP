package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.ChartOfAccount;
import com.yab.multitenantERP.repositories.ChartOfAccountRepository;
import com.yab.multitenantERP.repositories.JournalEntryLineRepository;
import com.yab.multitenantERP.repositories.JournalEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChartOfAccountService {
    private final ChartOfAccountRepository coaRepo;

    public ChartOfAccountService(ChartOfAccountRepository coaRepo,
                                 JournalEntryRepository journalEntryRepository,
                                 JournalEntryLineRepository journalEntryLineRepository) {
        this.coaRepo = coaRepo;
    }

    public String createAccount(ChartOfAccount account) {
        if (coaRepo.existsByCode(account.getCode())) {
            throw new IllegalArgumentException("Account code already exists: " + account.getCode());
        }
        coaRepo.save(account);
        return "Account is recorded successfully";

    }

    @Transactional(readOnly = true)
    public Optional<ChartOfAccount> findById(Long id) {
        return coaRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ChartOfAccount> findByCode(String code) {
        return coaRepo.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<ChartOfAccount> findAll() {
        return coaRepo.findAll();
    }


}