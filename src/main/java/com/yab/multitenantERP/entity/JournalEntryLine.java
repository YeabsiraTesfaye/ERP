package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "journal_entry_lines", uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "account"}))
@Setter
@Getter
public class JournalEntryLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coa_id", nullable = false)
    private ChartOfAccount account;

    @Column(precision = 19, scale = 4)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4)
    private BigDecimal credit = BigDecimal.ZERO;

    private String lineDescription;
}
