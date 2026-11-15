
package com.t824t.nutritiontracker.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.t824t.nutritiontracker.entity.AppUser;

import com.t824t.nutritiontracker.entity.Meal;
import java.time.LocalDate;

import java.util.List;

@DataJpaTest
class MealRepositoryTest {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void testSetup() {
        assertTrue(true);
    }

    @Test
    void mealsShouldBeSeparatedByUser() {

        // ユーザーAを作成
        AppUser userA = new AppUser();
        userA.setUsername("userA");
        userA.setPassword("testPasswordA");
        userA = appUserRepository.save(userA);

        // ユーザーBを作成
        AppUser userB = new AppUser();
        userB.setUsername("userB");
        userB.setPassword("testPasswordB");
        userB = appUserRepository.save(userB);

        // ユーザーAの食事記録を作成
        Meal mealA = new Meal();

        mealA.setMealName("鶏むね肉");
        mealA.setCalories(200);
        mealA.setProtein(30.0);
        mealA.setFat(5.0);
        mealA.setCarbs(0.0);
        mealA.setMealDate(LocalDate.now());
        mealA.setUser(userA);

        mealRepository.save(mealA);


        // ユーザーBの食事記録を作成
        Meal mealB = new Meal();

        mealB.setMealName("鮭");
        mealB.setCalories(250);
        mealB.setProtein(25.0);
        mealB.setFat(15.0);
        mealB.setCarbs(0.0);
        mealB.setMealDate(LocalDate.now());
        mealB.setUser(userB);

        mealRepository.save(mealB);


        // ユーザーAの食事記録を検索
        List<Meal> mealsOfA = mealRepository.findByUserIdAndMealDate(
                userA.getId(),
                LocalDate.now()
        );

        // ユーザーAの食事記録が1件だけ取得されることを確認
        assertEquals(1, mealsOfA.size());

        // 取得した食事が「鶏むね肉」であることを確認
        assertEquals("鶏むね肉", mealsOfA.get(0).getMealName());


        // ユーザーBの食事記録を検索
        List<Meal> mealsOfB = mealRepository.findByUserIdAndMealDate(
                userB.getId(),
                LocalDate.now()
        );

        // ユーザーBの食事記録が1件だけ取得されることを確認
        assertEquals(1, mealsOfB.size());

        // 取得した食事が「鮭」であることを確認
        assertEquals("鮭", mealsOfB.get(0).getMealName());



    }
}
