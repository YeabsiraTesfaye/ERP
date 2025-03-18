package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.ApiPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiPermissionRepository extends JpaRepository<ApiPermission, Long> {
    @Query("SELECT ap FROM ApiPermission ap JOIN ap.roles r WHERE r.id = :roleId AND ap.apiEndpoint = :api")
    Optional<ApiPermission> findByRoleAndApi(@Param("roleId") Long roleId, @Param("api") String api);

//    @Query("SELECT COUNT(ap) FROM ApiPermission ap WHERE ap.role.id IN :roleIds AND ap.api = :api")
//    long countByRolesAndApi(@Param("roleIds") List<Long> roleIds, @Param("api") String api);
}
