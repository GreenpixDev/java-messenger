package ru.greenpix.messenger.user.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.greenpix.messenger.user.util.ResourceUtil.getResource;

@Tag("api")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void beforeAll() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("sample");
        user.setEmail("sample@gmail.com");
        user.setHashedPassword("$2a$10$Hpo6HMobTJZX8vk9LHJPTOudpMZ4xsNECGqEgYS7YYetwY9t4xNwa");
        user.setFullName("Sample Test User");
        user.setRegistrationTimestamp(LocalDateTime.now());
        userRepository.save(user);
    }

    @DisplayName("Успешная регистрация")
    @ParameterizedTest
    @ValueSource(strings = {
            "/request/POST/signup/pass.json",
            "/request/POST/signup/pass_full.json",
    })
    public void signupSuccessTest(String file) throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @DisplayName("Регистрация с пустым телом")
    @Test
    public void signupEmptyBodyTest() throws Exception {
        mockMvc.perform(post("/signup"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Регистрация с невалидными данными")
    @ParameterizedTest
    @ValueSource(strings = {
            "/request/POST/signup/short_password.json",
            "/request/POST/signup/invalid_email.json",
            "/request/POST/signup/future_birth_date.json",
    })
    public void signupBadRequestTest(String file) throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Регистрация существующего пользователя")
    @ParameterizedTest
    @ValueSource(strings = {
            "/request/POST/signup/duplicate_username.json",
            "/request/POST/signup/duplicate_email.json",
    })
    public void signupDuplicateUserTest(String file) throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешный вход в систему")
    @ParameterizedTest
    @ValueSource(strings = "/request/POST/signin/pass.json")
    public void signinSuccessTest(String file) throws Exception {
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @DisplayName("Вход в систему с пустым телом")
    @Test
    public void signinEmptyBodyTest() throws Exception {
        mockMvc.perform(post("/signin"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Вход в систему с пустым логином или паролем")
    @ParameterizedTest
    @ValueSource(strings = {
            "/request/POST/signin/invalid_username.json",
            "/request/POST/signin/invalid_password.json"
    })
    public void signinEmptyPasswordTest(String file) throws Exception {
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Вход в систему с неверным логином или паролем")
    @ParameterizedTest
    @ValueSource(strings = {
            "/request/POST/signin/wrong_username.json",
            "/request/POST/signin/wrong_password.json",
    })
    public void signinWrongCredentialsTest(String file) throws Exception {
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
