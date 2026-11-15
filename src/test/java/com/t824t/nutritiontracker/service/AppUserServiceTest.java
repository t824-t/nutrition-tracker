
package com.t824t.nutritiontracker.service;

import com.t824t.nutritiontracker.entity.AppUser;
import com.t824t.nutritiontracker.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserServiceTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_shouldEncodePassword() {

        String username = "testuser123";
        String password = "password123";

        appUserService.registerUser(username, password);

        AppUser savedUser = appUserRepository
                .findByUsername(username)
                .orElseThrow();

        assertNotEquals(password, savedUser.getPassword());

        assertTrue(passwordEncoder.matches(
                password,
                savedUser.getPassword()
        ));
    }
}
