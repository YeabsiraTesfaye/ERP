package com.yab.multitenantERP.entity;

import com.yab.multitenantERP.enums.Gender;
import com.yab.multitenantERP.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
@Builder
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ Ensures auto-generation
  @Column(name = "employee_id")
  private Long id;

  @NonNull
  private String firstName;

  @NonNull
  private String middleName;

  @NonNull
  private String lastName;

  private String photo;

  @NonNull
  private Gender gender;

  private LocalDate birthDate;

  private String nationalId;

  @NonNull
  private String tinNumber;

  private String email;

  @Builder.Default
  @NonNull
  private LocalDate dateOfHire = LocalDate.now();


  @NonNull
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;


  @ManyToOne
  @JoinColumn(name = "position_id", nullable = false)
  private Position position;


  @ManyToOne
  @JoinColumn(name = "branch_id", nullable = false)
  private Branch branch;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<AddressHistory> addressHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<DepartmentHistory> departmentHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<PositionHistory> positionHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<EmploymentTypeHistory> employmentTypeHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<SalaryHistory> salaryHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<LeaveHistory> leaveHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<EmergencyContactHistory> emergencyContactHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Document> documents;


  private Status status;
}
