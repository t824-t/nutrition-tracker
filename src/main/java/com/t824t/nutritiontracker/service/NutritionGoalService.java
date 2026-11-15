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


    public void saveGoal(NutritionGoal nutritionGoal) {

        NutritionGoal existingGoal = nutritionGoalRepository
                .findByUserId(nutritionGoal.getUser().getId())
                .orElse(null);
        if (existingGoal != null) {
            nutritionGoal.setId(existingGoal.getId());
        } else {
            nutritionGoal.setId(null);
        }

        nutritionGoalRepository.save(nutritionGoal);
    }

    public NutritionGoal getGoalByUser(Long userId) {

        return nutritionGoalRepository
                .findByUserId(userId)
                .orElse(new NutritionGoal());
    }
}