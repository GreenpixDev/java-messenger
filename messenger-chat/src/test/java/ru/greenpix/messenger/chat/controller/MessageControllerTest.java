package ru.greenpix.messenger.chat.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.MessageRepository;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.jwt.model.JwtUser;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.greenpix.messenger.chat.util.ResourceUtil.getJson;
import static ru.greenpix.messenger.chat.util.ResourceUtil.getResource;

// НЕ ДОДЕЛАНЫ
// Не были переписаны под изменившееся API
@Tag("api")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtManager jwtManager;

    private String token;
    private UUID uuid;

    @BeforeAll
    public void beforeAll() {
        uuid = UUID.fromString("01508627-d194-4a38-afb7-dfa381b26905");
        token = "Bearer " + jwtManager.generateToken(new JwtUser(uuid, "test"));

        chatRepository.deleteAll();

        GroupChat groupChat = new GroupChat();
        groupChat.setId(UUID.fromString("e5cbbc6a-a5d6-4d68-8eb5-cec6ddd11a42"));
        // groupChat.setMemberIds(Set.of(uuid));
        groupChat.setAdminUserId(uuid);
        groupChat.setAvatarId(UUID.fromString("52a6477f-ad81-4a16-9eb9-77d0bcdc0cc0"));
        groupChat.setName("Test Chat");
        groupChat.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        groupChat = chatRepository.save(groupChat);

        GroupChat groupChat2 = new GroupChat();
        groupChat2.setId(UUID.fromString("6c2915e5-cd8d-49be-9bbf-57dc7a8d4739"));
        // groupChat2.setMemberIds(Set.of(uuid));
        groupChat2.setAdminUserId(uuid);
        groupChat2.setName("Second Chat");
        groupChat2.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        groupChat2 = chatRepository.save(groupChat2);

        GroupChat forbiddenChat = new GroupChat();
        forbiddenChat.setId(UUID.fromString("d21f3d0d-d98a-4563-aba5-13bdf56b582a"));
        forbiddenChat.setAdminUserId(UUID.fromString("bc416540-fe3d-43ce-8c90-2fa4e47ac5ab"));
        forbiddenChat.setName("Forbidden Chat");
        forbiddenChat.setCreationTimestamp(LocalDateTime.of(2002, 1, 1, 0, 0));
        chatRepository.save(forbiddenChat);

        Message message = new Message();
        message.setId(UUID.fromString("25b4aa64-1558-43ed-9e29-48c2a92d8156"));
        message.setChat(groupChat);
        message.setSenderId(uuid);
        message.setText("Hello");
        message.setCreationTimestamp(LocalDateTime.of(2001, 1, 1, 0, 0));
        messageRepository.save(message);

        Message message2 = new Message();
        message2.setChat(groupChat2);
        message2.setSenderId(uuid);
        message2.setText("Hello World!");
        message2.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        messageRepository.save(message2);
    }

    /*
     *
     */

    @DisplayName("Успешное и корректное получение сообщений переписки")
    @Test
    public void testGetMessageOfChat() throws Exception {
        mockMvc.perform(get("/chats/e5cbbc6a-a5d6-4d68-8eb5-cec6ddd11a42/messages")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJson("/response/GET/chats/messages.json")));
    }

    @DisplayName("Попытка получение сообщения чужой переписки")
    @Test
    public void testGetMessageOfForbiddenChat() throws Exception {
        mockMvc.perform(get("/chats/d21f3d0d-d98a-4563-aba5-13bdf56b582a/messages")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("Попытка получение сообщения несуществующей переписки")
    @Test
    public void testGetMessageOfNotExistsChat() throws Exception {
        mockMvc.perform(get("/chats/cb0c4042-a9ad-43d2-948e-70f368e93a36/messages")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    /*
     *
     */

    @DisplayName("Успешный и корректный поиск сообщений")
    @Test
    public void testSearchMessages() throws Exception {
        mockMvc.perform(get("/chats/messages?page=1&size=10&textFilter=heLLO")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJson("/response/GET/chats/messages/search.json")));
    }

    @DisplayName("Успешный поиск сообщений без параметров")
    @Test
    public void testSearchMessagesWithoutParams() throws Exception {
        mockMvc.perform(get("/chats/messages")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    /*
     *
     */

    @DisplayName("Успешная отправка сообщения в групповой чат")
    @Test
    public void sendGroupMessage() throws Exception {
        mockMvc.perform(post("/chats/e5cbbc6a-a5d6-4d68-8eb5-cec6ddd11a42/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/message.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("Отправка сообщения в несуществующий групповой чат")
    @Test
    public void sendGroupMessageToNotExistsChat() throws Exception {
        mockMvc.perform(post("/chats/ede018fa-bdf5-4947-8bec-3b9a67a24228/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/message.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("Отправка сообщения в групповой чат, в котором нет пользователя")
    @Test
    public void sendGroupMessageToForbiddenChat() throws Exception {
        mockMvc.perform(post("/chats/d21f3d0d-d98a-4563-aba5-13bdf56b582a/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/message.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    /*
     *
     */

    @DisplayName("Успешная отправка сообщения в личный чат")
    @Test
    public void sendPrivateMessage() throws Exception {
        mockMvc.perform(post("/users/693c4437-0257-47c9-9fba-59991012f2ca/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/message.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("Отправка сообщения в личный чат пользователю, который не является другом")
    @Test
    public void sendPrivateMessageToNotFriend() throws Exception {
        mockMvc.perform(post("/users/ccec4140-c07e-492f-a6be-c7f75bc5aa3e/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/message.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isForbidden());
    }
}
