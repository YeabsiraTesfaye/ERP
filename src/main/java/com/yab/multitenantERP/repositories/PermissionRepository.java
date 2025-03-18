package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Method to find permissions by a set of names
    Set<Permission> findByNameIn(Set<String> names);
}
