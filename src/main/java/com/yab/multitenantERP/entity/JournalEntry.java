package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.EntryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journal_entries", uniqueConstraints = @UniqueConstraint(columnNames = {"reference"}))

@Setter
@Getter
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private LocalDate postingDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    @OneToMany(
            mappedBy = "journalEntry",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<JournalEntryLine> lines;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "chart_of_account_id")
    @JsonIgnore
    private ChartOfAccount chartOfAccount;

}
