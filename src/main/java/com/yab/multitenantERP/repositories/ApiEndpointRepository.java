package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiEndpointRepository extends JpaRepository<ApiEntity, Long> {
    Optional<ApiEntity> findByPathAndMethod(String path, String method);
}