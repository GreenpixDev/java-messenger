package ru.greenpix.messenger.user.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            "/signup/pass.json",
            "/signup/pass_full.json",
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
            "/signup/short_password.json",
            "/signup/invalid_email.json",
            "/signup/future_birth_date.json",
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
            "/signup/duplicate_username.json",
            "/signup/duplicate_email.json",
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
    @ValueSource(strings = "/signin/pass.json")
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
            "/signin/invalid_username.json",
            "/signin/invalid_password.json"
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
            "/signin/wrong_username.json",
            "/signin/wrong_password.json",
    })
    public void signinWrongCredentialsTest(String file) throws Exception {
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private byte[] getResource(String name) {
        try {
            return Files.readAllBytes(Path.of(Objects.requireNonNull(getClass().getResource(name)).toURI()));
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
