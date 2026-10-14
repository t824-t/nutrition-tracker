package com.t824t.nutritiontracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class NutritionGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "目標カロリーを入力してください")
    @Min(value = 1, message = "目標カロリーは1以上で入力してください")
    private Integer targetCalories;

    @NotNull(message = "目標たんぱく質を入力してください")
    @DecimalMin(value = "0.1", message = "目標たんぱく質は0より大きい値を入力してください")
    private Double targetProtein;

    @NotNull(message = "目標脂質を入力してください")
    @DecimalMin(value = "0.1", message = "目標脂質は0より大きい値を入力してください")
    private Double targetFat;

    @NotNull(message = "目標炭水化物を入力してください")
    @DecimalMin(value = "0.1", message = "目標炭水化物は0より大きい値を入力してください")
    private Double targetCarbs;

    public NutritionGoal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(Integer targetCalories) {
        this.targetCalories = targetCalories;
    }

    public Double getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(Double targetProtein) {
        this.targetProtein = targetProtein;
    }

    public Double getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(Double targetFat) {
        this.targetFat = targetFat;
    }

    public Double getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(Double targetCarbs) {
        this.targetCarbs = targetCarbs;
    }
}