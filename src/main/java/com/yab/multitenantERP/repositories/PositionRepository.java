package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Department;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.entity.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PositionRepository extends JpaRepository<Position, Long> {
    @EntityGraph(attributePaths = {"department"})
    List<Position> findByDepartmentId(@NonNull Long id);

}

