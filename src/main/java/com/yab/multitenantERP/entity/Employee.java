package com.yab.multitenantERP.entity;

import com.yab.multitenantERP.enums.*;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  private Status maritalStatus;

  @Builder.Default
  private SalaryType salaryType=SalaryType.BY_POSITION;

  @Builder.Default
  private IncentiveType incentiveType = IncentiveType.BY_POSITION;

  @NonNull
  private String tinNumber;

  private String email;

  private String nationality;

  @Builder.Default
  @NonNull
  private LocalDate dateOfHire = LocalDate.now();

  @NonNull
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @ManyToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @ManyToOne
  @JoinColumn(name = "branch_id", nullable = false)
  private Branch branch;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private Salary salary;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Document> documents;

  @ManyToOne
  @JoinColumn(name = "manager_id")
  private Employee manager;

  @ManyToOne
  @JoinColumn(name = "supervisor_id")
  private Employee supervisor;

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
  private Address address;

//  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//  @OrderBy("id DESC")
//  private List<Address> addressHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OrderBy("id DESC")
  private List<Deduction> deductions;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "employee_benefit",
          joinColumns = @JoinColumn(name = "employee_id"),
          inverseJoinColumns = @JoinColumn(name = "benefit_id"))
  private List<Benefit> benefits;


  private Status status;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "shift_id")
  private Shift shift;

  @ManyToOne
  @JoinColumn(name = "employee_group_id")
  private EmployeeGroup employeeGroup;

}
