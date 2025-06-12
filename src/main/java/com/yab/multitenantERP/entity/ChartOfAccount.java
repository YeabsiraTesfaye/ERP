package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "chart_of_accounts", uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "code"}))
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ChartOfAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @OneToMany(mappedBy = "chartOfAccount")
    @JsonIgnore
    private List<JournalEntry> journalEntries;

    private String description;

    @Builder.Default
    private BigDecimal amount = BigDecimal.valueOf(0);
}
