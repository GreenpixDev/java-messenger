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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.common.service.JwtService;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.repository.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private String token;

    @BeforeAll
    public void beforeAll() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("sample");
        user.setEmail("sample@gmail.com");
        user.setHashedPassword("$2a$10$Hpo6HMobTJZX8vk9LHJPTOudpMZ4xsNECGqEgYS7YYetwY9t4xNwa");
        user.setFullName("Sample Test User");
        user.setRegistrationTimestamp(LocalDateTime.now());
        user = userRepository.save(user);
        token = "Bearer " + jwtService.generateToken(new JwtUser(user.getId(), user.getUsername()));
    }

    @DisplayName("Успешное получение своего профиля")
    @Test
    public void getMyselfTest() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Получение своего профиля без авторизации")
    @Test
    public void getMyselfUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Успешное редактирование своего профиля")
    @ParameterizedTest
    @ValueSource(strings = {
            "/users/me/pass.json",
            "/users/me/pass_full.json",
    })
    public void updateMyselfTest(String file) throws Exception {
        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Редактирование своего профиля с невалидными данными")
    @ParameterizedTest
    @ValueSource(strings = "/users/me/empty_fullname.json")
    public void invalidUpdateMyselfTest(String file) throws Exception {
        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource(file))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
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
