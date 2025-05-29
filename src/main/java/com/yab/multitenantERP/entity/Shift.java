package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;

    @JsonIgnore
    @OneToMany(mappedBy = "shift")
    private List<ShiftAssignment> shiftAssignments;

    @JsonIgnore
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private List<Employee> employees;

    @JsonIgnore
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private List<Position> positions;

//    @JsonIgnore
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private List<AttendancePolicy> attendancePolicies;  // Link to attendance policies

    @Builder.Default
    private boolean isDefault = false;
}
