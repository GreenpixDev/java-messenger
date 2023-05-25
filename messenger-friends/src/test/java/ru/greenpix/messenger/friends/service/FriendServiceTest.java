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
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.entity.Relationship;
import ru.greenpix.messenger.friends.exception.AdditionFriendException;
import ru.greenpix.messenger.friends.exception.AdditionYourselfAsFriendException;
import ru.greenpix.messenger.friends.exception.DeletionFriendException;
import ru.greenpix.messenger.friends.exception.FriendNotFoundException;
import ru.greenpix.messenger.friends.integration.users.client.UsersClient;
import ru.greenpix.messenger.friends.mapper.FilterMapper;
import ru.greenpix.messenger.friends.repository.FriendRepository;
import ru.greenpix.messenger.friends.service.impl.FriendServiceImpl;
import ru.greenpix.messenger.friends.settings.NotificationSettings;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

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
    static Specification<Friend> SPEC_TEST = BaseSpecification.empty();
    static Page<Friend> PAGE_FRIEND_TEST = Page.empty();
    static Friend FRIEND_TEST = new Friend();
    static Friend DELETED_FRIEND_TEST = new Friend();
    static FriendSearchDto FRIEND_SEARCH_DTO_TEST = new FriendSearchDto(LOCAL_DATE_TEST, STRING_TEST);

    static {
        DELETED_FRIEND_TEST.setDeletionDate(LOCAL_DATE_TEST);
    }

    /*
     * Заглушки
     */

    @Mock
    FriendRepository friendRepository;
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
    FriendServiceImpl friendService;

    /*
     * Тесты
     */

    @DisplayName("Проверка получения страницы друзей")
    @Test
    void getFriendPageTest() {
        when(friendRepository.findAllByDeletionDateNullAndRelationshipTargetUserIdAndFullNameLikeIgnoreCase(
                any(),
                eq(ID_TEST),
                eq("%" + STRING_TEST + "%")
        )).thenReturn(PAGE_FRIEND_TEST);

        Page<Friend> page = friendService.getFriendPage(ID_TEST, INT_TEST, INT_TEST, STRING_TEST);
        assertEquals(PAGE_FRIEND_TEST, page);
    }

    @DisplayName("Проверка получения страницы друзей с расширенными параметрами поиска")
    @Test
    void searchFriendPageTest() {
        when(mapper.toFriendSpecification(FRIEND_SEARCH_DTO_TEST))
                .thenReturn(SPEC_TEST);
        when(friendRepository.findAll(eq(SPEC_TEST), eq(PageRequest.of(INT_TEST, INT_TEST))))
                .thenReturn(PAGE_FRIEND_TEST);

        Page<Friend> page = friendService.getFriendPage(ID_TEST, INT_TEST, INT_TEST, FRIEND_SEARCH_DTO_TEST);
        assertEquals(PAGE_FRIEND_TEST, page);
    }

    @DisplayName("Проверка получения информации о друге")
    @Test
    void getFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(FRIEND_TEST));
        Friend result = friendService.getFriend(ID_TEST, ID_TEST_2);
        assertEquals(FRIEND_TEST, result);
    }

    @DisplayName("Проверка получения информации о несуществующем друге")
    @Test
    void getNonExistsFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.empty());
        assertThrows(FriendNotFoundException.class, () -> friendService.getFriend(ID_TEST, ID_TEST_2));
    }

    @DisplayName("Проверка добавления пользователя в друзья")
    @Test
    void addFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.empty());
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(usersClient.getUserFullName(eq(ID_TEST_2))).thenReturn(Optional.of(STRING_TEST));

        friendService.addFriend(ID_TEST, ID_TEST_2);

        verify(friendRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка добавления самого себя в друзья")
    @Test
    void addFriendYourselfTest() {
        assertThrows(AdditionYourselfAsFriendException.class, () -> friendService.addFriend(ID_TEST, ID_TEST));

        verify(friendRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @DisplayName("Проверка добавления пользователя в друзья, который уже добавлен")
    @Test
    void addAdditionFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(FRIEND_TEST));

        assertThrows(AdditionFriendException.class, () -> friendService.addFriend(ID_TEST, ID_TEST_2));

        verify(friendRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }

    @DisplayName("Проверка удаления пользователя из друзей")
    @Test
    void deleteFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(FRIEND_TEST));
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());

        friendService.deleteFriend(ID_TEST, ID_TEST_2);

        verify(friendRepository, times(1)).save(any());
        verify(notificationProducer, times(1)).sendNotification(any());
    }

    @DisplayName("Проверка удаления пользователя из друзей, который уже не в друзьях")
    @Test
    void deleteDeletionFriendTest() {
        when(friendRepository.findById(eq(REL_ID_TEST))).thenReturn(Optional.of(DELETED_FRIEND_TEST));

        assertThrows(DeletionFriendException.class, () -> friendService.deleteFriend(ID_TEST, ID_TEST_2));

        verify(friendRepository, never()).save(any());
        verify(notificationProducer, never()).sendNotification(any());
    }
}
