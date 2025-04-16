package com.yab.multitenantERP.entity;

import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String dayName;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceSession session;

}
