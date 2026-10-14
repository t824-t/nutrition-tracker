package com.t824t.nutritiontracker.repository;

import com.t824t.nutritiontracker.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeightRecordRepository
        extends JpaRepository<WeightRecord, Long> {

    List<WeightRecord> findAllByOrderByRecordDateAsc();

    WeightRecord findFirstByRecordDate(LocalDate recordDate);
}