package com.t824t.nutritiontracker;

import com.t824t.nutritiontracker.entity.Meal;
import com.t824t.nutritiontracker.entity.NutritionGoal;
import com.t824t.nutritiontracker.entity.WeightRecord;
import com.t824t.nutritiontracker.service.MealService;
import com.t824t.nutritiontracker.service.NutritionGoalService;
import com.t824t.nutritiontracker.service.WeightRecordService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.t824t.nutritiontracker.service.AppUserService;

@Controller
public class HomeController {

    private final MealService mealService;
    private final NutritionGoalService nutritionGoalService;
    private final WeightRecordService weightRecordService;
    private final AppUserService appUserService;


    public HomeController(
            MealService mealService,
            NutritionGoalService nutritionGoalService,
            WeightRecordService weightRecordService,
            AppUserService appUserService) {

        this.mealService = mealService;
        this.nutritionGoalService = nutritionGoalService;
        this.weightRecordService = weightRecordService;
        this.appUserService = appUserService;
    }


    // =========================
    // トップページ
    // =========================

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "new") String sort,
            @RequestParam(required = false) LocalDate date,
            Model model,
            Authentication authentication) {

        String username = authentication.getName();

        model.addAttribute("username", username);

        Long userId = appUserService.getUserByUsername(username).getId();

        LocalDate selectedDate =
                (date != null) ? date : LocalDate.now();

        prepareHomeModel(
                model,
                selectedDate,
                keyword,
                page,
                sort,
                userId
        );


        // 食事登録フォーム
        Meal meal = new Meal();
        meal.setMealDate(selectedDate);

        model.addAttribute(
                "meal",
                meal
        );


        // 体重登録フォーム
        WeightRecord weightRecord =
                new WeightRecord();

        weightRecord.setRecordDate(
                selectedDate
        );

        model.addAttribute(
                "weightRecord",
                weightRecord
        );


        return "index";
    }


    // =========================
    // index.html 共通Model
    // =========================

    private void prepareHomeModel(
            Model model,
            LocalDate selectedDate,
            String keyword,
            int page,
            String sort,
            Long userId) {


        // =========================
        // 食事一覧
        // =========================

        Page<Meal> mealPage;

        if (keyword == null || keyword.isBlank()) {

            mealPage =
                    mealService.getMealsByUserAndDate(
                            userId,
                            selectedDate,
                            page,
                            sort
                    );

        } else {

            mealPage =
                    mealService.searchMealsByUserAndDateAndKeyword(
                            userId,
                            selectedDate,
                            keyword,
                            page,
                            sort
                    );
        }


        model.addAttribute(
                "meals",
                mealPage.getContent()
        );

        model.addAttribute(
                "currentPage",
                page
        );

        model.addAttribute(
                "totalPages",
                mealPage.getTotalPages()
        );


        // =========================
        // 選択日の栄養合計
        // =========================

        int totalCalories =
                mealService.getTotalCaloriesByUser(
                        userId,
                        selectedDate
                );

        double totalProtein =
                mealService.getTotalProteinByUser(
                        userId,
                        selectedDate
                );

        double totalFat =
                mealService.getTotalFatByUser(
                        userId,
                        selectedDate
                );

        double totalCarbs =
                mealService.getTotalCarbsByUser(
                        userId,
                        selectedDate
                );


        model.addAttribute(
                "totalCalories",
                totalCalories
        );

        model.addAttribute(
                "totalProtein",
                totalProtein
        );

        model.addAttribute(
                "totalFat",
                totalFat
        );

        model.addAttribute(
                "totalCarbs",
                totalCarbs
        );


        // =========================
        // 基本情報
        // =========================

        model.addAttribute(
                "selectedDate",
                selectedDate
        );

        model.addAttribute(
                "keyword",
                keyword
        );

        model.addAttribute(
                "sort",
                sort
        );


        // =========================
        // 体重履歴
        // =========================

        List<WeightRecord> weightRecords =
                weightRecordService.getWeightsByUser(userId);

        model.addAttribute(
                "weightRecords",
                weightRecords
        );


        // =========================
        // 栄養目標
        // =========================

        NutritionGoal nutritionGoal;

        if (model.containsAttribute("nutritionGoal")) {

            nutritionGoal =
                    (NutritionGoal) model.getAttribute(
                            "nutritionGoal"
                    );

        } else {

            nutritionGoal =
                    nutritionGoalService.getGoalByUser(userId);

            model.addAttribute(
                    "nutritionGoal",
                    nutritionGoal
            );
        }


        // =========================
        // 進捗率
        // =========================

        int calorieProgress = 0;
        int proteinProgress = 0;
        int fatProgress = 0;
        int carbsProgress = 0;


        Integer targetCalories = nutritionGoal != null
                ? nutritionGoal.getTargetCalories()
                : null;

        if (targetCalories != null && targetCalories > 0) {

            calorieProgress =
                    (int) Math.min(
                            100,
                            totalCalories
                                    * 100.0
                                    / targetCalories
                    );
        }


        if (nutritionGoal != null
                && nutritionGoal.getTargetProtein() != null
                && nutritionGoal.getTargetProtein() > 0) {

            proteinProgress =
                    (int) Math.min(
                            100,
                            totalProtein
                                    * 100.0
                                    / nutritionGoal.getTargetProtein()
                    );
        }


        if (nutritionGoal != null
                && nutritionGoal.getTargetFat() != null
                && nutritionGoal.getTargetFat() > 0) {

            fatProgress =
                    (int) Math.min(
                            100,
                            totalFat
                                    * 100.0
                                    / nutritionGoal.getTargetFat()
                    );
        }


        if (nutritionGoal != null
                && nutritionGoal.getTargetCarbs() != null
                && nutritionGoal.getTargetCarbs() > 0) {

            carbsProgress =
                    (int) Math.min(
                            100,
                            totalCarbs
                                    * 100.0
                                    / nutritionGoal.getTargetCarbs()
                    );
        }


        model.addAttribute(
                "calorieProgress",
                calorieProgress
        );

        model.addAttribute(
                "proteinProgress",
                proteinProgress
        );

        model.addAttribute(
                "fatProgress",
                fatProgress
        );

        model.addAttribute(
                "carbsProgress",
                carbsProgress
        );
    }


    // =========================
    // 食事登録
    // =========================

    @PostMapping("/meals")
    public String createMeal(
            @Valid
            @ModelAttribute("meal")
            Meal meal,
            BindingResult result,
            Model model,
            Authentication authentication) {

        if (result.hasErrors()) {

            LocalDate selectedDate =
                    meal.getMealDate() != null
                            ? meal.getMealDate()
                            : LocalDate.now();


            prepareHomeModel(
                    model,
                    selectedDate,
                    null,
                    0,
                    "new",
                    appUserService.getUserByUsername(
                            org.springframework.security.core.context.SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()
                    ).getId()
            );


            model.addAttribute(
                    "meal",
                    meal
            );


            WeightRecord weightRecord =
                    new WeightRecord();

            weightRecord.setRecordDate(
                    selectedDate
            );

            model.addAttribute(
                    "weightRecord",
                    weightRecord
            );


            return "index";
        }


        meal.setUser(
                appUserService.getUserByUsername(
                        authentication.getName()
                )
        );

        mealService.saveMeal(meal);


        return "redirect:/?date="
                + meal.getMealDate();
    }


    // =========================
    // 食事詳細
    // =========================

    @GetMapping("/detail/{id}")
    public String detailMeal(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        Long userId = appUserService
                .getUserByUsername(authentication.getName())
                .getId();

        Meal meal =
                mealService.getMealByIdAndUser(
                        id,
                        userId
                );

        model.addAttribute(
                "meal",
                meal
        );

        return "detail";
    }


    // =========================
    // 食事編集
    // =========================

    @GetMapping("/edit/{id}")
    public String editMeal(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        Long userId = appUserService
                .getUserByUsername(authentication.getName())
                .getId();

        Meal meal =
                mealService.getMealByIdAndUser(
                        id,
                        userId
                );

        model.addAttribute(
                "meal",
                meal
        );

        return "edit";
    }


    // =========================
    // 食事更新
    // =========================

    @PostMapping("/update/{id}")
    public String updateMeal(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("meal")
            Meal meal,
            BindingResult result,
            Model model,
            Authentication authentication) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "meal",
                    meal
            );

            return "edit";
        }


        Long userId = appUserService
                .getUserByUsername(authentication.getName())
                .getId();

        Meal existingMeal = mealService.getMealByIdAndUser(
                id,
                userId
        );

        meal.setId(existingMeal.getId());
        meal.setUser(existingMeal.getUser());

        mealService.saveMeal(meal);


        return "redirect:/?date="
                + meal.getMealDate();
    }


    // =========================
    // 食事削除
    // =========================

    @PostMapping("/delete/{id}")
    public String deleteMeal(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = appUserService
                .getUserByUsername(authentication.getName())
                .getId();

        Meal meal =
                mealService.getMealByIdAndUser(
                        id,
                        userId
                );

        LocalDate mealDate =
                meal.getMealDate();


        mealService.deleteMeal(
                id,
                userId
        );

        return "redirect:/?date="
                + mealDate;
    }


    // =========================
    // 栄養目標保存
    // =========================

    @PostMapping("/goal")
    public String saveGoal(
            @Valid
            @ModelAttribute("nutritionGoal")
            NutritionGoal nutritionGoal,
            BindingResult result,
            Model model,
            Authentication authentication) {

        // ===== バリデーション確認用 =====
        System.out.println("===== NutritionGoal validation =====");
        System.out.println("targetCalories = " + nutritionGoal.getTargetCalories());
        System.out.println("targetProtein  = " + nutritionGoal.getTargetProtein());
        System.out.println("targetFat      = " + nutritionGoal.getTargetFat());
        System.out.println("targetCarbs    = " + nutritionGoal.getTargetCarbs());
        System.out.println("hasErrors      = " + result.hasErrors());
        System.out.println("errorCount     = " + result.getErrorCount());

        result.getAllErrors().forEach(error ->
                System.out.println("ERROR: " + error.getDefaultMessage())
        );


        if (result.hasErrors()) {

            LocalDate selectedDate =
                    LocalDate.now();


            prepareHomeModel(
                    model,
                    selectedDate,
                    null,
                    0,
                    "new",
                    appUserService.getUserByUsername(
                            org.springframework.security.core.context.SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()
                    ).getId()
            );

            // 入力されたNutritionGoalを
            // バリデーション結果ごと画面へ戻す
            model.addAttribute(
                    "nutritionGoal",
                    nutritionGoal
            );


            Meal meal =
                    new Meal();

            meal.setMealDate(
                    selectedDate
            );

            model.addAttribute(
                    "meal",
                    meal
            );


            WeightRecord weightRecord =
                    new WeightRecord();

            weightRecord.setRecordDate(
                    selectedDate
            );

            model.addAttribute(
                    "weightRecord",
                    weightRecord
            );


            return "index";
        }


        nutritionGoal.setUser(
                appUserService.getUserByUsername(
                        authentication.getName()
                )
        );

        nutritionGoalService.saveGoal(
                nutritionGoal
        );


        return "redirect:/";
    }


    // =========================
    // 体重登録・更新
    // =========================

    @PostMapping("/weight")
    public String saveWeight(
            @Valid
            @ModelAttribute("weightRecord")
            WeightRecord weightRecord,
            BindingResult result,
            Model model,
            Authentication authentication) {

        if (result.hasErrors()) {

            LocalDate selectedDate =
                    weightRecord.getRecordDate() != null
                            ? weightRecord.getRecordDate()
                            : LocalDate.now();


            prepareHomeModel(
                    model,
                    selectedDate,
                    null,
                    0,
                    "new",
                    appUserService.getUserByUsername(
                            org.springframework.security.core.context.SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()
                    ).getId()
            );


            Meal meal =
                    new Meal();

            meal.setMealDate(
                    selectedDate
            );

            model.addAttribute(
                    "meal",
                    meal
            );


            model.addAttribute(
                    "weightRecord",
                    weightRecord
            );


            return "index";
        }


        weightRecord.setUser(
                appUserService.getUserByUsername(
                        authentication.getName()
                )
        );

        weightRecordService.saveWeight(
                weightRecord
        );


        return "redirect:/?date="
                + weightRecord.getRecordDate();
    }


    // =========================
    // 体重削除
    // =========================

    @PostMapping("/weight/delete/{id}")
    public String deleteWeight(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = appUserService
                .getUserByUsername(authentication.getName())
                .getId();

        weightRecordService.deleteWeight(id, userId);


        return "redirect:/";
    }
}