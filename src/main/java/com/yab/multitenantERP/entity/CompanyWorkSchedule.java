package com.yab.multitenantERP.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> weekends;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_schedule_id") // ✅ Foreign key reference
    private List<Holiday> holidays;
}
