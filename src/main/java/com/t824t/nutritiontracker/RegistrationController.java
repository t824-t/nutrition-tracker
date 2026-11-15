package com.t824t.nutritiontracker;

import com.t824t.nutritiontracker.service.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final AppUserService appUserService;

    public RegistrationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (username.isBlank()
                || username.length() < 3
                || username.length() > 50) {

            model.addAttribute(
                    "error",
                    "ユーザー名は3〜50文字で入力してください"
            );

            return "register";
        }
        if (password.length() < 8) {
            model.addAttribute("error", "パスワードは8文字以上で入力してください");
            return "register";
        }

        if (appUserService.existsByUsername(username)) {
            model.addAttribute("error", "そのユーザー名は既に使われています");
            return "register";
        }

        appUserService.registerUser(username, password);

        return "redirect:/login?registered";
    }
}