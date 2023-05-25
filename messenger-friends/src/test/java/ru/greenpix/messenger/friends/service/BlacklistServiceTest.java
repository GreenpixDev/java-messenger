package ru.greenpix.messenger.friends.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionBlockedUserException;
import ru.greenpix.messenger.friends.exception.AdditionYourselfAsBlockedUserException;
import ru.greenpix.messenger.friends.exception.BlockedUserNotFoundException;
import ru.greenpix.messenger.friends.exception.DeletionBlockedUserException;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.mapper.FilterMapper;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.friends.service.impl.BlacklistServiceImpl;
import ru.greenpix.messenger.friends.settings.NotificationSettings;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class BlacklistServiceTest {

    /*
     * Тестовые данные
     */

    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static UUID ID_TEST_2 = UUID.fromString("1da6f9a6-4547-4769-b33c-06746f396d89");
    static Relationship REL_ID_TEST = new Relationship(ID_TEST, ID_TEST_2);
    static String STRING_TEST = "Test";
    static int INT_TEST = 25;
    static LocalDate LOCAL_DATE_TEST = LocalDate.of(2000, 1, 1);
    static Specification<BlockedUser> SPEC_TEST = BaseSpecification.empty();
    static Page<BlockedUser> PAGE_BLOCKED_USER_TEST = Page.empty();
    static BlockedUser BLOCKED_USER_TEST = new BlockedUser();
    static BlockedUser DELETED_BLOCKED_USER_TEST = new BlockedUser();
    static BlockedUserSearchDto BLOCKED_USER_SEARCH_DTO_TEST = new BlockedUserSearchDto(LOCAL_DATE_TEST, STRING_TEST);

    static {
        DELETED_BLOCKED_USER_TEST.setDeletionDate(LOCAL_DATE_TEST);
    }

    /*
     * Заглушки
     */

    @Mock
    BlacklistRepository blacklistRepository;
    @Mock
    Clock clock;
    @Mock
    UsersClient usersClient;
    @Mock
    FilterMapper mapper;
    @Mock
    NotificationProducer notificationProducer;
    @Mock
    NotificationSettings notificationSettings;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    BlacklistServiceImpl blacklistService;

    /*
     * Тесты
     */

    @DisplayName("Проверка получения страницы друзей")
    @Test
    void getFriendPageTest() {
        when(blacklistRepository.findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
                any(),
                eq(ID_TEST),
                eq("%" + STRING_TEST + "%")
        )).thenReturn(PAGE_BLOCKED_USER_TEST);

        Page<BlockedUser> page = blacklistService.getBlockedUserPage(ID_TEST, INT_TEST, INT_TEST, STRING_TEST);
        assertEquals(PAGE_BLOCKED_USER_TEST, page);
    }

    @DisplayName("Проверка получения страницы друзей с расширенными параметрами поиска")
    @Test
    void searchFriendPageTest() {
        when(mapper.toBlockedUserSpecification(BLOCKED_USER_SEARCH_DTO_TEST))
                .thenReturn(SPEC_TEST);
        when(blacklistRepository.findAll(eq(SPEC_TEST), eq(PageRequest.of(INT_TEST, INT_TEST))))
                .thenReturn(PAGE_BLOCKED_USER_TEST);

        Page<BlockedUser> page = blacklistService.getBlockedUserPage(ID_TEST, INT_TEST, INT_TEST, BLOCKED_USER_SEARCH_DTO_TEST);
        assertEquals(PAGE_BLOCKED_USER_TEST, page);
    }

    @DisplayName("Проверка получения информации о друге")
    @Test
    void getFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(BLOCKED_USER_TEST));
        BlockedUser result = blacklistService.getBlockedUser(ID_TEST, ID_TEST_2);
        assertEquals(BLOCKED_USER_TEST, result);
    }

    @DisplayName("Проверка получения информации о несуществующем друге")
    @Test
    void getNonExistsFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.empty());
        assertThrows(BlockedUserNotFoundException.class, () -> blacklistService.getBlockedUser(ID_TEST, ID_TEST_2));
    }

    @DisplayName("Проверка добавления пользователя в друзья")
    @Test
    void addFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.empty());
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(usersClient.getUserFullName(eq(ID_TEST_2))).thenReturn(Optional.of(STRING_TEST));

        blacklistService.addBlockedUser(ID_TEST, ID_TEST_2);

        verify(blacklistRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка добавления самого себя в друзья")
    @Test
    void addFriendYourselfTest() {
        assertThrows(AdditionYourselfAsBlockedUserException.class, () -> blacklistService.addBlockedUser(ID_TEST, ID_TEST));

        verify(blacklistRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @DisplayName("Проверка добавления пользователя в друзья, который уже добавлен")
    @Test
    void addAdditionFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(BLOCKED_USER_TEST));

        assertThrows(AdditionBlockedUserException.class, () -> blacklistService.addBlockedUser(ID_TEST, ID_TEST_2));

        verify(blacklistRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @DisplayName("Проверка удаления пользователя из друзей")
    @Test
    void deleteFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(BLOCKED_USER_TEST));
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());

        blacklistService.deleteBlockedUser(ID_TEST, ID_TEST_2);

        verify(blacklistRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка удаления пользователя из друзей, который уже не в друзьях")
    @Test
    void deleteDeletionFriendTest() {
        when(blacklistRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(DELETED_BLOCKED_USER_TEST));

        assertThrows(DeletionBlockedUserException.class, () -> blacklistService.deleteBlockedUser(ID_TEST, ID_TEST_2));

        verify(blacklistRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }
}
