package com.t824t.nutritiontracker.repository;

import com.t824t.nutritiontracker.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


public interface MealRepository extends JpaRepository<Meal, Long> {




    Page<Meal> findByUserIdAndMealDate(
            Long userId,
            LocalDate mealDate,
            Pageable pageable
    );

    Page<Meal> findByUserIdAndMealDateAndMealNameContaining(
            Long userId,
            LocalDate mealDate,
            String keyword,
            Pageable pageable
    );

    List<Meal> findByUserIdAndMealDate(
            Long userId,
            LocalDate mealDate
    );

    Optional<Meal> findByIdAndUserId(
            Long id,
            Long userId
    );
}