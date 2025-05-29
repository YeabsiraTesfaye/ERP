package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.AttendanceSession;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceSession session;

    private LocalTime lateAfter;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;  // Associate policy with a specific shift
}
