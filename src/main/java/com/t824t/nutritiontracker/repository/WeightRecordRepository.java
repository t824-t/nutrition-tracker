package com.t824t.nutritiontracker.repository;
import com.t824t.nutritiontracker.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeightRecordRepository
        extends JpaRepository<WeightRecord, Long> {

    List<WeightRecord> findAllByOrderByRecordDateAsc();

    WeightRecord findFirstByRecordDate(LocalDate recordDate);

    List<WeightRecord> findByUserIdOrderByRecordDateDesc(
            Long userId
    );

    Optional<WeightRecord> findByIdAndUserId(
            Long id,
            Long userId
    );

    WeightRecord findFirstByUserIdAndRecordDate(
            Long userId,
            LocalDate recordDate
    );
}