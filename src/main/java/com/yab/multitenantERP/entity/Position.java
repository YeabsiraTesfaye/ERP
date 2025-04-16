package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(name = "position_name", nullable = false)
    private String name;

    @Column(name = "required_manpower")
    private Integer requiredManPower;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    private List<Employee> employees;

    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "position_benefit",
            joinColumns = @JoinColumn(name = "position_id"),
            inverseJoinColumns = @JoinColumn(name = "benefit_id"))
    private List<Benefit> benefits;



//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "position_allowance",
//            joinColumns = @JoinColumn(name = "position_id"),
//            inverseJoinColumns = @JoinColumn(name = "allowance_id"))
//    private List<Allowance> allowances;


    @OneToOne(mappedBy = "position", cascade = CascadeType.ALL)
    private Salary salary;
}
