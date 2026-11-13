package com.t824t.nutritiontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.t824t.nutritiontracker.service.AppUserService;
import org.springframework.context.annotation.Profile;
@SpringBootApplication
public class NutritionTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NutritionTrackerApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner initUser(AppUserService appUserService) {
        return args -> {

            if (!appUserService.existsByUsername("taikaku")) {
                appUserService.registerUser(
                        "taikaku",
                        System.getenv("DEV_USER_PASSWORD")
                );
                System.out.println("開発用ユーザーを登録しました");
            }

        };
    }

}
