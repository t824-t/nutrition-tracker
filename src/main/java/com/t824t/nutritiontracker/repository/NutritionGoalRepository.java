package com.t824t.nutritiontracker.repository;

import com.t824t.nutritiontracker.entity.NutritionGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutritionGoalRepository
        extends JpaRepository<NutritionGoal, Long> {

    Optional<NutritionGoal> findByUserId(Long userId);

}