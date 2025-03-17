package com.yab.multitenantERP.entity;

import com.yab.multitenantERP.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;
  private String lastName;
  private Gender gender;
  private LocalDate birthDate;
  private String nationalId;
  private String tinNumber;

  private String email;
  private String phoneNumber;


  @ManyToOne
  @JoinColumn(name = "branch_id", nullable = false)
  private Branch branch;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<AddressHistory> addressHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<DepartmentHistory> departmentHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<PositionHistory> positionHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<EmploymentTypeHistory> employmentTypeHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<SalaryHistory> salaryHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<LeaveHistory> leaveHistory;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<EmergencyContactHistory> emergencyContactHistory;



  private boolean isActive = true;
}
