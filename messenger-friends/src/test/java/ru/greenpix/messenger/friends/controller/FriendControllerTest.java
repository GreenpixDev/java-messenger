package ru.greenpix.messenger.friends.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.repository.FriendRepository;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.jwt.model.JwtUser;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("e2e")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private JwtManager jwtManager;

    private String token;

    private final UUID nonExistsUserId = UUID.fromString("dd4fc5ea-2970-4abc-a6ff-f91992319ec4");
    private final UUID userId = UUID.fromString("040c7d07-0593-4dfd-9b53-ed702e208b05");
    private final UUID nonFriendId = UUID.fromString("82dd46c1-9f34-4070-84b5-113b5a0838d8");
    private final UUID friendId = UUID.fromString("561b8fbb-f2e6-47b3-81c3-83835d7b054f");
    private final UUID friendToAddId = UUID.fromString("3cfb4589-2977-4174-ba4a-e15d0d442518");
    private final UUID friendToDeleteId = UUID.fromString("646295c6-483e-41e9-ae79-e685e6321c78");

    @BeforeAll
    public void beforeAll() {
        friendRepository.deleteAll();

        Friend friend = new Friend();
        friend.setRelationship(new Relationship(userId, friendId));
        friend.setFullName("Иванов Иван Иванович");
        friend.setAdditionDate(LocalDate.now());
        friendRepository.save(friend);

        Friend friendToDelete = new Friend();
        friendToDelete.setRelationship(new Relationship(userId, friendToDeleteId));
        friendToDelete.setFullName("Иванов Иван Иванович");
        friendToDelete.setAdditionDate(LocalDate.now());
        friendRepository.save(friendToDelete);

        token = "Bearer " + jwtManager.generateToken(new JwtUser(userId, "test"));
    }

    @DisplayName("Успешное получение списка друзей")
    @Test
    public void getFriendListTest(String file) throws Exception {
        mockMvc.perform(get("/users/me/friends?page=1&size=10"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Успешное получение списка друзей с фильтром по ФИО")
    @Test
    public void getFriendListWithFullNameFilterTest() throws Exception {
        mockMvc.perform(get("/users/me/friends?page=1&size=10&filterFullName=иван")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Получение списка друзей с неположительной страницей")
    @Test
    public void getFriendListNotPositivePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка друзей с неположительным размером страницы")
    @Test
    public void getFriendListNotPositiveSizePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends?page=1&size=0")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка друзей со слишком большим размером страницы")
    @Test
    public void getFriendListVeryBigSizePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends?page=1&size=" + Integer.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешное получение информации о друге")
    @Test
    public void getFriendDetailsTest(String file) throws Exception {
        mockMvc.perform(get("/users/me/friends/" + friendId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Получение информации о несуществующем друге")
    @Test
    public void getNotExistsFriendDetailsTest(String file) throws Exception {
        mockMvc.perform(get("/users/me/friends/" + nonFriendId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("Успешное добавление друга")
    @Test
    public void addFriendTest(String file) throws Exception {
        mockMvc.perform(post("/users/me/friends/" + friendToAddId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка добавления друга, который уже является другом")
    @Test
    public void addAlreadyFriendTest(String file) throws Exception {
        mockMvc.perform(post("/users/me/friends/" + friendId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление себя как друга")
    @Test
    public void addYourselfAsFriendTest(String file) throws Exception {
        mockMvc.perform(post("/users/me/friends/" + userId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Добавление несуществующего пользователя как друга")
    @Test
    public void addNotExistsUserFriendTest(String file) throws Exception {
        mockMvc.perform(post("/users/me/friends/" + nonExistsUserId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Успешная синхронизация друга")
    @Test
    public void synchronizeFriendTest(String file) throws Exception {
        mockMvc.perform(patch("/users/me/friends/" + friendId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка синхронизации друга, который не является другом")
    @Test
    public void synchronizeNotFriendTest(String file) throws Exception {
        mockMvc.perform(patch("/users/me/friends/" + nonFriendId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Успешное удаление друга")
    @Test
    public void deleteFriendTest(String file) throws Exception {
        mockMvc.perform(delete("/users/me/friends/" + friendToDeleteId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка удаления друга, который не является другом")
    @Test
    public void deleteNotFriendTest(String file) throws Exception {
        mockMvc.perform(delete("/users/me/friends/" + nonFriendId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешный поиск друзей")
    @Test
    public void searchFriendsTest(String file) throws Exception {
        mockMvc.perform(get("/users/me/friends/search?page=1&size=10")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("Успешный поиск друзей с фильтрами")
    @Test
    public void searchFriendsWithFiltersTest() throws Exception {
        mockMvc.perform(get("/users/me/friends/search?page=1&size=10&filterFullName=test")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Поиск друзей с неположительной страницей")
    @Test
    public void searchFriendsNotPositivePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends/search?page=0&size=10")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Поиск друзей с неположительным размером страницы")
    @Test
    public void searchFriendsNotPositiveSizePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends/search?page=1&size=0")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Поиск друзей со слишком большим размером страницы")
    @Test
    public void searchFriendsVeryBigSizePageTest() throws Exception {
        mockMvc.perform(get("/users/me/friends/search?page=1&size=" + Integer.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
