package ru.greenpix.messenger.chat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.entity.PrivateChat;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.IllegalChatTypeException;
import ru.greenpix.messenger.chat.exception.UserBlockedException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.ChatMapper;
import ru.greenpix.messenger.chat.mapper.ChatMemberMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.GroupChatRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.impl.ChatServiceImpl;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

import javax.persistence.Tuple;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    /*
     * Тестовые данные
     */

    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static String STRING_TEST = "Test";
    static int INT_TEST = 25;
    static Page<Tuple> PAGE_TUPLE_TEST = Page.empty();
    static GroupChat GROUP_CHAT_TEST = new GroupChat();
    static PrivateChat PRIVATE_CHAT_TEST = new PrivateChat();
    static ChatMember CHAT_MEMBER_TEST = new ChatMember();
    static ModificationChatDto MODIFICATION_CHAT_DTO_TEST = new ModificationChatDto(STRING_TEST, List.of(ID_TEST));
    static UserIntegrationDto USER_INTEGRATION_DTO_TEST = new UserIntegrationDto(ID_TEST, STRING_TEST, ID_TEST);

    /*
     * Заглушки
     */

    @Mock
    Clock clock;
    @Mock
    ChatRepository chatRepository;
    @Mock
    GroupChatRepository groupChatRepository;
    @Mock
    PrivateChatRepository privateChatRepository;
    @Mock
    FriendsClient friendsClient;
    @Mock
    UsersClient usersClient;
    @Mock
    ChatMapper chatMapper;
    @Mock
    ChatMemberMapper chatMemberMapper;
    @Mock
    Logger logger;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    ChatServiceImpl chatService;

    /*
     * Тесты
     */

    @DisplayName("Проверка получения доступных пользователю чатов")
    @Test
    void getAccessibleChatTest() {
        when(chatRepository.findAllWithLastMessageOrderByLastMessageTime(
                eq(ID_TEST),
                eq("%" + STRING_TEST + "%"),
                eq(PageRequest.of(INT_TEST, INT_TEST))
        )).thenReturn(PAGE_TUPLE_TEST);

        Page<ChatDto> page = chatService.getAccessibleChat(ID_TEST, INT_TEST, INT_TEST, STRING_TEST);
        assertEquals(Page.empty(), page);
    }

    @DisplayName("Проверка получения детальной информации о чате")
    @Test
    void getChatTest() {
        /*when(chatRepository.findAllWithLastMessage(
                eq(ID_TEST),
                eq(STRING_TEST),
                eq(PageRequest.of(INT_TEST, INT_TEST))
        )).thenReturn(PAGE_TUPLE_TEST);

        ChatDetailsDto dto = chatService.getChat(ID_TEST, ID_TEST);
        assertEquals(Page.empty(), page);*/
        // TODO
    }

    @DisplayName("Проверка создания чата")
    @Test
    void createChatTest() {
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(usersClient.getUsers(anyCollection())).thenReturn(List.of(USER_INTEGRATION_DTO_TEST));
        when(friendsClient.isBlockedByUser(any(), eq(ID_TEST))).thenReturn(false);
        when(chatMemberMapper.toChatMember(any(), any())).thenReturn(CHAT_MEMBER_TEST);
        when(chatRepository.save(any())).thenReturn(GROUP_CHAT_TEST);

        assertDoesNotThrow(() -> chatService.createChat(ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, times(1)).save(any());
    }

    @DisplayName("Проверка создания чата с добавлением пользователя, который заблокировал создателя чата")
    @Test
    void createChatWithBlockedUserTest() {
        when(usersClient.getUsers(anyCollection())).thenReturn(List.of(USER_INTEGRATION_DTO_TEST));
        when(friendsClient.isBlockedByUser(any(), eq(ID_TEST))).thenReturn(true);

        assertThrows(UserBlockedException.class, () -> chatService.createChat(ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, never()).save(any());
    }

    @DisplayName("Проверка обновления чата")
    @Test
    void updateChatTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST))).thenReturn(Optional.of(GROUP_CHAT_TEST));
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(usersClient.getUsers(anyCollection())).thenReturn(List.of(USER_INTEGRATION_DTO_TEST));
        when(friendsClient.isBlockedByUser(any(), eq(ID_TEST))).thenReturn(false);
        when(chatMemberMapper.toChatMember(any(), any())).thenReturn(CHAT_MEMBER_TEST);
        when(chatRepository.save(any())).thenReturn(GROUP_CHAT_TEST);

        assertDoesNotThrow(() -> chatService.updateChat(ID_TEST, ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, times(1)).save(any());
    }

    @DisplayName("Проверка обновления несуществующего чата")
    @Test
    void updateNonExistsChatTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST))).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> chatService.updateChat(ID_TEST, ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, never()).save(any());
    }

    @DisplayName("Проверка обновления личных сообщений")
    @Test
    void updatePrivateChatTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST))).thenReturn(Optional.of(PRIVATE_CHAT_TEST));

        assertThrows(IllegalChatTypeException.class, () -> chatService.updateChat(ID_TEST, ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, never()).save(any());
    }

    @DisplayName("Проверка обновления чата с добавлением пользователя, который заблокировал создателя чата")
    @Test
    void updateChatWithBlockedUserTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST))).thenReturn(Optional.of(GROUP_CHAT_TEST));
        when(usersClient.getUsers(anyCollection())).thenReturn(List.of(USER_INTEGRATION_DTO_TEST));
        when(friendsClient.isBlockedByUser(any(), eq(ID_TEST))).thenReturn(true);

        assertThrows(UserBlockedException.class, () -> chatService.updateChat(ID_TEST, ID_TEST, MODIFICATION_CHAT_DTO_TEST, null));
        verify(chatRepository, never()).save(any());
    }
}
