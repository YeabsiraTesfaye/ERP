package com.yab.multitenantERP.entity;

import com.yab.multitenantERP.enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String reference;
    private String description;

    @Enumerated(EnumType.STRING)
    private InventoryTransactionType type; // INBOUND, OUTBOUND, ADJUSTMENT

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<InventoryTransactionLine> lines;

    @ManyToOne
    private Warehouse warehouse;

    private Boolean posted = false;
}
