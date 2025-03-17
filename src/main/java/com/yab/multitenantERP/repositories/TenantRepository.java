package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
