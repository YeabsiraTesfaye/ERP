package com.yab.multitenantERP.repositories;

import com.yab.multitenantERP.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByYear_Id(Long yearId);
}