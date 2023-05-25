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
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.chat.entity.PrivateChat;
import ru.greenpix.messenger.chat.exception.ChatNotFoundException;
import ru.greenpix.messenger.chat.exception.UserBlockedException;
import ru.greenpix.messenger.chat.exception.UserNotFriendException;
import ru.greenpix.messenger.chat.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.chat.integration.storage.client.FileStorageClient;
import ru.greenpix.messenger.chat.integration.users.client.UsersClient;
import ru.greenpix.messenger.chat.mapper.ChatMemberMapper;
import ru.greenpix.messenger.chat.mapper.MessageMapper;
import ru.greenpix.messenger.chat.repository.ChatRepository;
import ru.greenpix.messenger.chat.repository.MessageRepository;
import ru.greenpix.messenger.chat.repository.PrivateChatRepository;
import ru.greenpix.messenger.chat.service.impl.MessageServiceImpl;
import ru.greenpix.messenger.chat.settings.NotificationSettings;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

import javax.persistence.Tuple;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {


    /*
     * Тестовые данные
     */

    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static UUID ID_TEST_2 = UUID.fromString("1da6f9a6-4547-4769-b33c-06746f396d89");
    static String STRING_TEST = "Test";
    static int INT_TEST = 25;
    static Page<Tuple> PAGE_TUPLE_TEST = Page.empty();
    static Page<MessageDto> PAGE_MESSAGE_DTO_TEST = Page.empty();
    static GroupChat GROUP_CHAT_TEST = new GroupChat();
    static PrivateChat PRIVATE_CHAT_TEST = new PrivateChat();
    static Message MESSAGE_TEST = new Message();
    static ChatMember CHAT_MEMBER_TEST = new ChatMember();
    static UserIntegrationDto USER_INTEGRATION_DTO_TEST = new UserIntegrationDto(ID_TEST, STRING_TEST, ID_TEST);
    static UserIntegrationDto USER_INTEGRATION_DTO_TEST_2 = new UserIntegrationDto(ID_TEST_2, STRING_TEST, ID_TEST_2);
    static SendingMessageDto SENDING_MESSAGE_DTO_TEST = new SendingMessageDto(STRING_TEST);

    /*
     * Заглушки
     */

    @Mock
    ChatRepository chatRepository;
    @Mock
    PrivateChatRepository privateChatRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    MessageMapper messageMapper;
    @Mock
    ChatMemberMapper chatMemberMapper;
    @Mock
    UsersClient usersClient;
    @Mock
    FriendsClient friendsClient;
    @Mock
    FileStorageClient fileStorageClient;
    @Mock
    NotificationProducer notificationProducer;
    @Mock
    NotificationSettings notificationSettings;
    @Mock
    Clock clock;
    @Mock
    Logger logger;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    MessageServiceImpl messageService;

    /*
     * Тесты
     */

    @DisplayName("Проверка получения всех сообщений у пользователя")
    @Test
    void getMessagesTest() {
        when(messageRepository.findAllWithChatNameAndAttachmentNames(
                eq(ID_TEST),
                eq("%" + STRING_TEST + "%"),
                any())
        ).thenReturn(PAGE_TUPLE_TEST);

        Page<MessageDto> page = messageService.getMessages(ID_TEST, INT_TEST, INT_TEST, STRING_TEST);
        assertEquals(PAGE_MESSAGE_DTO_TEST, page);
    }

    @DisplayName("Проверка получения сообщений в переписке")
    @Test
    void getChatMessagesTest() {
        /*when(chatRepository.existsIdAndMember(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(true);
        when(messageRepository.findAllByChatId(eq(ID_TEST), any(Pageable.class)))
                .thenReturn(PAGE_MESSAGE_TEST);

        List<MessageDetailsDto> list = messageService.getChatMessages(ID_TEST, ID_TEST);
        assertEquals(PAGE_MESSAGE_DTO_TEST, page);*/

        // TODO
    }

    @DisplayName("Проверка отправки сообщения в личные сообщения")
    @Test
    void sendPrivateMessageTest() {
        when(friendsClient.isBlockedByUser(eq(ID_TEST_2), eq(ID_TEST)))
                .thenReturn(false);
        when(friendsClient.isFriend(eq(ID_TEST), eq(ID_TEST_2)))
                .thenReturn(true);
        when(usersClient.getUsers(anyCollection()))
                .thenReturn(List.of(USER_INTEGRATION_DTO_TEST, USER_INTEGRATION_DTO_TEST_2));
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(chatMemberMapper.toChatMember(any(), any()))
                .thenReturn(CHAT_MEMBER_TEST);
        when(privateChatRepository.save(any()))
                .thenReturn(PRIVATE_CHAT_TEST);
        when(messageMapper.toMessageEntity(eq(SENDING_MESSAGE_DTO_TEST)))
                .thenReturn(MESSAGE_TEST);

        assertDoesNotThrow(
                () -> messageService.sendPrivateMessage(ID_TEST, ID_TEST_2, SENDING_MESSAGE_DTO_TEST, new MultipartFile[0])
        );

        verify(privateChatRepository, times(1)).save(any());
        verify(messageRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка отправки сообщения в личные сообщения с вложениями")
    @Test
    void sendPrivateMessageWithAttachmentsTest() {
        MultipartFile[] files = new MultipartFile[] { mock(MultipartFile.class) };

        when(friendsClient.isBlockedByUser(eq(ID_TEST_2), eq(ID_TEST)))
                .thenReturn(false);
        when(friendsClient.isFriend(eq(ID_TEST), eq(ID_TEST_2)))
                .thenReturn(true);
        when(usersClient.getUsers(anyCollection()))
                .thenReturn(List.of(USER_INTEGRATION_DTO_TEST, USER_INTEGRATION_DTO_TEST_2));
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(chatMemberMapper.toChatMember(any(), any()))
                .thenReturn(CHAT_MEMBER_TEST);
        when(privateChatRepository.save(any()))
                .thenReturn(PRIVATE_CHAT_TEST);
        when(messageMapper.toMessageEntity(eq(SENDING_MESSAGE_DTO_TEST)))
                .thenReturn(MESSAGE_TEST);
        when(fileStorageClient.uploadFile(files[0]))
                .thenReturn(Optional.of(ID_TEST));

        assertDoesNotThrow(
                () -> messageService.sendPrivateMessage(ID_TEST, ID_TEST_2, SENDING_MESSAGE_DTO_TEST, files)
        );

        verify(privateChatRepository, times(1)).save(any());
        verify(messageRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка отправки сообщения в личные сообщения заблокированному пользователю")
    @Test
    void sendPrivateMessageToBlockedUserTest() {
        when(friendsClient.isBlockedByUser(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(true);

        assertThrows(UserBlockedException.class,
                () -> messageService.sendPrivateMessage(ID_TEST, ID_TEST, SENDING_MESSAGE_DTO_TEST, new MultipartFile[0])
        );

        verify(privateChatRepository, never()).save(any());
        verify(messageRepository, never()).save(any());
    }

    @DisplayName("Проверка отправки сообщения в личные сообщения не другу")
    @Test
    void sendPrivateMessageToNonFriendTest() {
        when(friendsClient.isBlockedByUser(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(false);
        when(friendsClient.isFriend(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(false);

        assertThrows(UserNotFriendException.class,
                () -> messageService.sendPrivateMessage(ID_TEST, ID_TEST, SENDING_MESSAGE_DTO_TEST, new MultipartFile[0])
        );

        verify(privateChatRepository, never()).save(any());
        verify(messageRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @DisplayName("Проверка отправки сообщения в групповой чат")
    @Test
    void sendGroupMessageTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(Optional.of(GROUP_CHAT_TEST));
        when(messageMapper.toMessageEntity(eq(SENDING_MESSAGE_DTO_TEST)))
                .thenReturn(MESSAGE_TEST);

        assertDoesNotThrow(
                () -> messageService.sendGroupMessage(ID_TEST, ID_TEST, SENDING_MESSAGE_DTO_TEST, new MultipartFile[0])
        );

        verify(messageRepository, times(1)).save(any());
    }

    @DisplayName("Проверка отправки сообщения в групповой чат с вложениями")
    @Test
    void sendGroupMessageWithAttachmentsTest() {
        MultipartFile[] files = new MultipartFile[] { mock(MultipartFile.class) };

        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(Optional.of(GROUP_CHAT_TEST));
        when(messageMapper.toMessageEntity(eq(SENDING_MESSAGE_DTO_TEST)))
                .thenReturn(MESSAGE_TEST);
        when(fileStorageClient.uploadFile(files[0]))
                .thenReturn(Optional.of(ID_TEST));

        assertDoesNotThrow(
                () -> messageService.sendGroupMessage(ID_TEST, ID_TEST, SENDING_MESSAGE_DTO_TEST, files)
        );

        verify(messageRepository, times(1)).save(any());
    }

    @DisplayName("Проверка отправки сообщения в несуществующий групповой чат")
    @Test
    void sendGroupMessageToNonExistsChatTest() {
        when(chatRepository.findIdAndMember(eq(ID_TEST), eq(ID_TEST)))
                .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class,
                () -> messageService.sendGroupMessage(ID_TEST, ID_TEST, SENDING_MESSAGE_DTO_TEST, new MultipartFile[0]
        ));

        verify(messageRepository, never()).save(any());
    }
}
