package com.t824t.nutritiontracker.service;

import com.t824t.nutritiontracker.entity.NutritionGoal;
import com.t824t.nutritiontracker.repository.NutritionGoalRepository;
import org.springframework.stereotype.Service;

@Service
public class NutritionGoalService {

    private final NutritionGoalRepository nutritionGoalRepository;

    public NutritionGoalService(
            NutritionGoalRepository nutritionGoalRepository) {

        this.nutritionGoalRepository = nutritionGoalRepository;
    }

    public NutritionGoal getGoal() {

        return nutritionGoalRepository
                .findAll()
                .stream()
                .findFirst()
                .orElse(new NutritionGoal());
    }

    public void saveGoal(NutritionGoal nutritionGoal) {

        NutritionGoal existingGoal = nutritionGoalRepository
                .findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (existingGoal != null) {
            nutritionGoal.setId(existingGoal.getId());
        }

        nutritionGoalRepository.save(nutritionGoal);
    }
}