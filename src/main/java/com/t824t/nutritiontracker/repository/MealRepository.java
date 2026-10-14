package com.t824t.nutritiontracker.repository;

import com.t824t.nutritiontracker.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findByMealNameContaining(
            String keyword,
            Pageable pageable
    );

    List<Meal> findByMealDate(LocalDate mealDate);

    Page<Meal> findByMealDate(
            LocalDate mealDate,
            Pageable pageable
    );

    Page<Meal> findByMealDateAndMealNameContaining(
            LocalDate mealDate,
            String keyword,
            Pageable pageable
    );
}