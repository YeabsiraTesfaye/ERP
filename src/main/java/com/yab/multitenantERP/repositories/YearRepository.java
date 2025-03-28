package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Holiday;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.entity.Year;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YearRepository extends JpaRepository<Year, Long> {
    @Query("SELECT y FROM Year y WHERE y.name = :year")
    public Optional<Year> findByYear(@Param("year") @NonNull String year);
}
