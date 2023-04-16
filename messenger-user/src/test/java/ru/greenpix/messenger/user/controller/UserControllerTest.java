package ru.greenpix.messenger.user.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.common.service.JwtService;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private String token;
    private UUID uuid;

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
        uuid = user.getId();
        token = "Bearer " + jwtService.generateToken(new JwtUser(user.getId(), user.getUsername()));
    }

    @DisplayName("Успешное получение списка профилей")
    @Test
    public void getUserPageTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=10")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Успешное получение списка профилей с сортировкой и фильтрами")
    @Test
    public void getUserPageWithSortAndFilterTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=10&sort=birth_date:desc&sort=id:asc&filter=city:test&filter=phone:test")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Получение списка профилей с неположительной страницей")
    @Test
    public void getNotPositiveUserPageTest() throws Exception {
        mockMvc.perform(get("/users?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка профилей с неположительным размером страницы")
    @Test
    public void getNotPositiveSizeUserPageTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=0")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка профилей со слишком большим размером страницы")
    @Test
    public void getVeryBigSizeUserPageTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=" + Integer.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка профилей с несуществующей сортировкой")
    @Test
    public void getUserPageWithInvalidSortTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=10&sort=invalid:desc")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка профилей с несуществующим фильтром")
    @Test
    public void getUserPageWithInvalidFilterTest() throws Exception {
        mockMvc.perform(get("/users?page=1&size=10&filter=invalid:test")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешное получение информации о пользователе")
    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/users/" + uuid)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Получение информации о несуществующем пользователе")
    @Test
    public void getNotExistsUserTest() throws Exception {
        mockMvc.perform(get("/users/41ce4301-4fc7-4d96-b33d-32a018ca5a55")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
