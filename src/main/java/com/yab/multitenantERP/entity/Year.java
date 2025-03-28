package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "years")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the year, e.g., "2023"
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Holiday> holidays;
}
