package com.t824t.nutritiontracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "食事名を入力してください")
    private String mealName;

    @NotNull(message = "日付を入力してください")
    private LocalDate mealDate;

    @NotNull(message = "カロリーを入力してください")
    @Min(value = 0, message = "カロリーは0以上で入力してください")
    private Integer calories;

    @NotNull(message = "たんぱく質を入力してください")
    @DecimalMin(value = "0.0", message = "たんぱく質は0以上で入力してください")
    private Double protein;

    @NotNull(message = "脂質を入力してください")
    @DecimalMin(value = "0.0", message = "脂質は0以上で入力してください")
    private Double fat;

    @NotNull(message = "炭水化物を入力してください")
    @DecimalMin(value = "0.0", message = "炭水化物は0以上で入力してください")
    private Double carbs;


    public Meal() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }


    public LocalDate getMealDate() {
        return mealDate;
    }

    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }


    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }


    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }


    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }


    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }
}