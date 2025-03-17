//package com.yab.multitenantERP.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Set;
//
//@Entity
//@Table(name = "tenants")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Tenant {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Ensures AUTO_INCREMENT
//    @Column(name = "tenant_id", nullable = false, updatable = false)
//    private Long id;
//
//    @Column(name = "name", unique = true, nullable = false)
//    private String name;
//
//}
package com.yab.multitenantERP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
