package com.t824t.nutritiontracker;

import com.t824t.nutritiontracker.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PageRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserService appUserService;

    private static final String USERNAME = "page-test-user";

    @BeforeEach
    void setup() {
        if (!appUserService.existsByUsername(USERNAME)) {
            appUserService.registerUser(USERNAME, "TestPassword123!");
        }
    }

    @Test
    void allPagesRenderSuccessfully() throws Exception {
        var authentication =
            new UsernamePasswordAuthenticationToken(
                USERNAME,
                null,
                java.util.List.of(
                    new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")
                )
            );

        var context =
            org.springframework.security.core.context.SecurityContextHolder
                .createEmptyContext();
        context.setAuthentication(authentication);

        for (String path : new String[]{"/", "/meals", "/weights", "/goals"}) {
            mockMvc.perform(
                get(path).with(request -> {
                    request.getSession().setAttribute(
                        org.springframework.security.web.context.HttpSessionSecurityContextRepository
                            .SPRING_SECURITY_CONTEXT_KEY,
                        context
                    );
                    return request;
                })
            ).andExpect(status().isOk());

            System.out.println("PAGE OK: " + path);
        }
    }
}
