package com.t824t.nutritiontracker;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.mock.web.MockHttpSession;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedUserRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginAndRegistrationPagesArePublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void registrationWithoutCsrfIsForbidden() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "csrf-test-user")
                        .param("password", "TestPassword123!"))
                .andExpect(status().isForbidden());
    }

    @Test
    void h2ConsoleIsNotPublic() throws Exception {
        mockMvc.perform(get("/h2-console/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void registrationAndLoginWithCsrfSucceed() throws Exception {
        String username = "auth-" + UUID.randomUUID().toString().substring(0, 12);
        String password = "TestPassword123!";

        var registerPage = mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) registerPage.getRequest().getSession();

        CsrfToken csrf = (CsrfToken) registerPage.getRequest()
                .getAttribute(CsrfToken.class.getName());

        if (csrf == null) {
            throw new AssertionError("CSRFトークンが取得できません");
        }

        mockMvc.perform(post("/register")
                        .session(session)
                        .param(csrf.getParameterName(), csrf.getToken())
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection());

        var loginPage = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession loginSession =
                (MockHttpSession) loginPage.getRequest().getSession();

        CsrfToken loginCsrf = (CsrfToken) loginPage.getRequest()
                .getAttribute(CsrfToken.class.getName());

        if (loginCsrf == null) {
            throw new AssertionError("ログイン用CSRFトークンが取得できません");
        }

        mockMvc.perform(post("/login")
                        .session(loginSession)
                        .param(loginCsrf.getParameterName(), loginCsrf.getToken())
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}
