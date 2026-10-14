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

@Controller
public class HomeController {

    private final MealService mealService;
    private final NutritionGoalService nutritionGoalService;
    private final WeightRecordService weightRecordService;


    public HomeController(
            MealService mealService,
            NutritionGoalService nutritionGoalService,
            WeightRecordService weightRecordService) {

        this.mealService = mealService;
        this.nutritionGoalService = nutritionGoalService;
        this.weightRecordService = weightRecordService;
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
            Model model) {

        LocalDate selectedDate =
                (date != null) ? date : LocalDate.now();

        prepareHomeModel(
                model,
                selectedDate,
                keyword,
                page,
                sort
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
            String sort) {


        // =========================
        // 食事一覧
        // =========================

        Page<Meal> mealPage;

        if (keyword == null || keyword.isBlank()) {

            mealPage =
                    mealService.getMealsByDate(
                            selectedDate,
                            page,
                            sort
                    );

        } else {

            mealPage =
                    mealService.searchMealsByDateAndKeyword(
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
                mealService.getTotalCalories(
                        selectedDate
                );

        double totalProtein =
                mealService.getTotalProtein(
                        selectedDate
                );

        double totalFat =
                mealService.getTotalFat(
                        selectedDate
                );

        double totalCarbs =
                mealService.getTotalCarbs(
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
                weightRecordService.getAllWeights();

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
                    nutritionGoalService.getGoal();

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


        if (nutritionGoal.getTargetCalories() != null
                && nutritionGoal.getTargetCalories() > 0) {

            calorieProgress =
                    (int) Math.min(
                            100,
                            totalCalories
                                    * 100.0
                                    / nutritionGoal.getTargetCalories()
                    );
        }


        if (nutritionGoal.getTargetProtein() != null
                && nutritionGoal.getTargetProtein() > 0) {

            proteinProgress =
                    (int) Math.min(
                            100,
                            totalProtein
                                    * 100.0
                                    / nutritionGoal.getTargetProtein()
                    );
        }


        if (nutritionGoal.getTargetFat() != null
                && nutritionGoal.getTargetFat() > 0) {

            fatProgress =
                    (int) Math.min(
                            100,
                            totalFat
                                    * 100.0
                                    / nutritionGoal.getTargetFat()
                    );
        }


        if (nutritionGoal.getTargetCarbs() != null
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
            Model model) {

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
                    "new"
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


        mealService.saveMeal(
                meal
        );


        return "redirect:/?date="
                + meal.getMealDate();
    }


    // =========================
    // 食事詳細
    // =========================

    @GetMapping("/detail/{id}")
    public String detailMeal(
            @PathVariable Long id,
            Model model) {

        Meal meal =
                mealService.getMealById(
                        id
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
            Model model) {

        Meal meal =
                mealService.getMealById(
                        id
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
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "meal",
                    meal
            );

            return "edit";
        }


        meal.setId(
                id
        );

        mealService.saveMeal(
                meal
        );


        return "redirect:/?date="
                + meal.getMealDate();
    }


    // =========================
    // 食事削除
    // =========================

    @PostMapping("/delete/{id}")
    public String deleteMeal(
            @PathVariable Long id) {

        Meal meal =
                mealService.getMealById(
                        id
                );

        LocalDate mealDate =
                meal.getMealDate();


        mealService.deleteMeal(
                id
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
            Model model) {

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
                    "new"
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
            Model model) {

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
                    "new"
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
            @PathVariable Long id) {

        weightRecordService.deleteWeight(
                id
        );


        return "redirect:/";
    }
}