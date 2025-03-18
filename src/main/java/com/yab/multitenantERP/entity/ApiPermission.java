package com.yab.multitenantERP.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "api_permissions")
public class ApiPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiEndpoint;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_api_permissions",
        joinColumns = @JoinColumn(name = "api_permission_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    // Getters and setters
}
