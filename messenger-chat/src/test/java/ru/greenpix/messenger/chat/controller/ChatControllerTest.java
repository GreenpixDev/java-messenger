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
import ru.greenpix.messenger.chat.entity.PrivateChat;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.jwt.model.JwtUser;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private JwtManager jwtManager;

    private String token;
    private UUID uuid;

    @BeforeAll
    public void beforeAll() {
        uuid = UUID.fromString("01508627-d194-4a38-afb7-dfa381b26905");
        token = "Bearer " + jwtManager.generateToken(new JwtUser(uuid, "test"));

        chatRepository.deleteAll();

        PrivateChat privateChat = new PrivateChat();
        privateChat.setId(UUID.fromString("f1d43686-a860-4a75-b6f8-35009f2389de"));
        // TODO privateChat.setMemberIds(Set.of(uuid, UUID.fromString("272d9bee-9d89-4635-b721-79e55ade20a1")));
        chatRepository.save(privateChat);

        GroupChat groupChat = new GroupChat();
        groupChat.setId(UUID.fromString("e5cbbc6a-a5d6-4d68-8eb5-cec6ddd11a42"));
        // TODO groupChat.setMemberIds(Set.of(uuid));
        groupChat.setAdminUserId(uuid);
        groupChat.setAvatarId(UUID.fromString("52a6477f-ad81-4a16-9eb9-77d0bcdc0cc0"));
        groupChat.setName("Test Chat");
        groupChat.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        chatRepository.save(groupChat);

        GroupChat groupChat2 = new GroupChat();
        groupChat2.setId(UUID.fromString("3d781101-d083-4b9c-9d59-95fda9af1701"));
        // TODO groupChat2.setMemberIds(Set.of(uuid));
        groupChat2.setAdminUserId(uuid);
        groupChat2.setName("Common chat");
        groupChat2.setCreationTimestamp(LocalDateTime.of(2001, 1, 1, 0, 0));
        chatRepository.save(groupChat2);

        GroupChat forbiddenChat = new GroupChat();
        groupChat.setId(UUID.fromString("d21f3d0d-d98a-4563-aba5-13bdf56b582a"));
        groupChat.setAdminUserId(UUID.fromString("bc416540-fe3d-43ce-8c90-2fa4e47ac5ab"));
        groupChat.setName("Forbidden Chat");
        groupChat.setCreationTimestamp(LocalDateTime.of(2002, 1, 1, 0, 0));
        chatRepository.save(forbiddenChat);
    }

    /*
     *
     */

    @DisplayName("Успешное и корректное получение списка переписок")
    @Test
    public void testGetChatPage() throws Exception {
        mockMvc.perform(get("/chats?page=1&size=10&nameFilter=teST")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJson("/response/GET/chats/search.json")));
    }

    @DisplayName("Успешное получение списка переписок без параметров")
    @Test
    public void testGetChatPageWithoutParams() throws Exception {
        mockMvc.perform(get("/chats")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    /*
     *
     */

    @DisplayName("Успешное и корректное получение информации о чате")
    @Test
    public void testGetChat() throws Exception {
        mockMvc.perform(get("/chats/e5cbbc6a-a5d6-4d68-8eb5-cec6ddd11a42")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(getJson("/response/GET/chats/chat_details.json")));
    }

    @DisplayName("Попытка получить информацию о чужом чате")
    @Test
    public void testTryGetProtectedChat() throws Exception {
        mockMvc.perform(get("/chats/d21f3d0d-d98a-4563-aba5-13bdf56b582a")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("Попытка получить информацию о несуществующем чате")
    @Test
    public void testTryGetNotExistsChat() throws Exception {
        mockMvc.perform(get("/chats/b34de622-54f7-459a-8082-88621b168597")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    /*
     *
     */

    @DisplayName("Успешное создание чата")
    @Test
    public void testCreateChat() throws Exception {
        mockMvc.perform(post("/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/modify_chat.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    /*
     *
     */

    @DisplayName("Успешное обновление чата")
    @Test
    public void testUpdateChat() throws Exception {
        GroupChat chat = new GroupChat();
        chat.setId(UUID.fromString("5a38146b-0bc4-44f9-9695-55691eda300a"));
        chat.setAdminUserId(uuid);
        // TODO chat.setMemberIds(Set.of(uuid));
        chat.setName("Old Name Of Chat");
        chat.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        chatRepository.save(chat);

        mockMvc.perform(put("/chats/5a38146b-0bc4-44f9-9695-55691eda300a")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content("/request/POST/chats/modify_chat.json")
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("Успешное обновление чата, к которому нет доступа")
    @Test
    public void testUpdateForbiddenChat() throws Exception {
        GroupChat chat = new GroupChat();
        chat.setId(UUID.fromString("5a38146b-0bc4-44f9-9695-55691eda300a"));
        chat.setAdminUserId(UUID.fromString("76fcb29f-8462-45de-b589-9ca19a3a818b"));
        chat.setName("Old Name Of Chat");
        chat.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        chatRepository.save(chat);

        mockMvc.perform(put("/chats/5a38146b-0bc4-44f9-9695-55691eda300a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/modify_chat.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("Успешное обновление чата, который не создал пользователь")
    @Test
    public void testUpdateChatByNotAdmin() throws Exception {
        GroupChat chat = new GroupChat();
        chat.setId(UUID.fromString("5a38146b-0bc4-44f9-9695-55691eda300a"));
        chat.setAdminUserId(UUID.fromString("76fcb29f-8462-45de-b589-9ca19a3a818b"));
        // TODO chat.setMemberIds(Set.of(uuid));
        chat.setName("Old Name Of Chat");
        chat.setCreationTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
        chatRepository.save(chat);

        mockMvc.perform(put("/chats/5a38146b-0bc4-44f9-9695-55691eda300a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getResource("/request/POST/chats/modify_chat.json"))
                        .header(HttpHeaders.AUTHORIZATION, token)
                )
                .andExpect(status().isForbidden());
    }

}
