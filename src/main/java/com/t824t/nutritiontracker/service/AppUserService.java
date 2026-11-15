package com.t824t.nutritiontracker.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.t824t.nutritiontracker.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import com.t824t.nutritiontracker.entity.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("ユーザーが見つかりません")
                );
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles("USER")
                .build();
    }

    public void registerUser(String username, String password) {

        AppUser user = new AppUser();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        appUserRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("ユーザーが見つかりません"));
    }
}