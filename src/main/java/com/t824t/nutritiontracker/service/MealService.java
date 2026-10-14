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

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public Page<Meal> getMealsByPage(int page, String sort) {

        Sort sortOrder;

        if (sort.equals("old")) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(
                page,
                10,
                sortOrder
        );

        return mealRepository.findAll(pageable);
    }

    public List<Meal> getMealsByDate(LocalDate mealDate) {
        return mealRepository.findByMealDate(mealDate);
    }

    public Page<Meal> getMealsByDate(
            LocalDate mealDate,
            int page,
            String sort) {

        Sort sortOrder;

        if (sort.equals("old")) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(
                page,
                10,
                sortOrder
        );

        return mealRepository.findByMealDate(
                mealDate,
                pageable
        );
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElseThrow();
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    // 今日の合計カロリー
    public int getTotalCalories(LocalDate mealDate) {

        List<Meal> meals = mealRepository.findByMealDate(mealDate);

        int total = 0;

        for (Meal meal : meals) {
            if (meal.getCalories() != null) {
                total += meal.getCalories();
            }
        }

        return total;
    }

    // 今日の合計たんぱく質
    public double getTotalProtein(LocalDate mealDate) {

        List<Meal> meals = mealRepository.findByMealDate(mealDate);

        double total = 0;

        for (Meal meal : meals) {
            if (meal.getProtein() != null) {
                total += meal.getProtein();
            }
        }

        return total;
    }

    // 今日の合計脂質
    public double getTotalFat(LocalDate mealDate) {

        List<Meal> meals = mealRepository.findByMealDate(mealDate);

        double total = 0;

        for (Meal meal : meals) {
            if (meal.getFat() != null) {
                total += meal.getFat();
            }
        }

        return total;
    }

    // 今日の合計炭水化物
    public double getTotalCarbs(LocalDate mealDate) {

        List<Meal> meals = mealRepository.findByMealDate(mealDate);

        double total = 0;

        for (Meal meal : meals) {
            if (meal.getCarbs() != null) {
                total += meal.getCarbs();
            }
        }

        return total;
    }

    public Page<Meal> searchMealsByPage(
            String keyword,
            int page,
            String sort) {

        Sort sortOrder;

        if (sort.equals("old")) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(
                page,
                10,
                sortOrder
        );

        return mealRepository.findByMealNameContaining(
                keyword,
                pageable
        );
    }

    public Page<Meal> searchMealsByDateAndKeyword(
            LocalDate mealDate,
            String keyword,
            int page,
            String sort) {

        Sort sortOrder;

        if (sort.equals("old")) {
            sortOrder = Sort.by("id").ascending();
        } else {
            sortOrder = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(
                page,
                10,
                sortOrder
        );

        return mealRepository.findByMealDateAndMealNameContaining(
                mealDate,
                keyword,
                pageable
        );
    }
}