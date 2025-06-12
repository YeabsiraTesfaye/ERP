package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yab.multitenantERP.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address_history")
public class AddressHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state_or_province")
    private String stateOrProvince;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "address_type")
    private AddressType addressType; // e.g., Permanent, Current, Mailing

    @Column(name = "sub_city")
    private String subCity;

    @Column(name = "woreda")
    private String woreda;

    @Column(name = "kebele")
    private String kebele;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
