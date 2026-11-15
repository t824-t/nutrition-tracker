package com.t824t.nutritiontracker.service;

import org.springframework.stereotype.Service;
import com.t824t.nutritiontracker.repository.MealRepository;
import com.t824t.nutritiontracker.entity.Meal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class MealService {

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public void saveMeal(Meal meal) {
        mealRepository.save(meal);
    }




    public void deleteMeal(Long id, Long userId) {

        Meal meal = getMealByIdAndUser(id, userId);

        mealRepository.delete(meal);
    }


    public Page<Meal> getMealsByUserAndDate(
            Long userId,
            LocalDate mealDate,
            int page,
            String sort
    ) {
        Sort sortOrder;

        if ("old".equals(sort)) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(page, 10, sortOrder);

        return mealRepository.findByUserIdAndMealDate(
                userId,
                mealDate,
                pageable
        );
    }

    public Page<Meal> searchMealsByUserAndDateAndKeyword(
            Long userId,
            LocalDate mealDate,
            String keyword,
            int page,
            String sort
    ) {
        Sort sortOrder;

        if ("old".equals(sort)) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(page, 10, sortOrder);

        return mealRepository.findByUserIdAndMealDateAndMealNameContaining(
                userId,
                mealDate,
                keyword,
                pageable
        );
    }

    public int getTotalCaloriesByUser(
            Long userId,
            LocalDate mealDate
    ) {
        List<Meal> meals =
                mealRepository.findByUserIdAndMealDate(
                        userId,
                        mealDate
                );

        return meals.stream()
                .mapToInt(Meal::getCalories)
                .sum();
    }

    public double getTotalProteinByUser(
            Long userId,
            LocalDate mealDate
    ) {
        List<Meal> meals =
                mealRepository.findByUserIdAndMealDate(
                        userId,
                        mealDate
                );

        return meals.stream()
                .mapToDouble(Meal::getProtein)
                .sum();
    }

    public double getTotalFatByUser(
            Long userId,
            LocalDate mealDate
    ) {
        List<Meal> meals =
                mealRepository.findByUserIdAndMealDate(
                        userId,
                        mealDate
                );

        return meals.stream()
                .mapToDouble(Meal::getFat)
                .sum();
    }

    public double getTotalCarbsByUser(
            Long userId,
            LocalDate mealDate
    ) {
        List<Meal> meals =
                mealRepository.findByUserIdAndMealDate(
                        userId,
                        mealDate
                );

        return meals.stream()
                .mapToDouble(Meal::getCarbs)
                .sum();
    }

    public Meal getMealByIdAndUser(
            Long mealId,
            Long userId
    ) {
        return mealRepository.findByIdAndUserId(
                mealId,
                userId
        ).orElseThrow(() ->
                new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "食事データが見つかりません"
                )
        );
    }
}