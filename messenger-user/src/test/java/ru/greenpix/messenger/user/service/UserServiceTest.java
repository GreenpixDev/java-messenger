package ru.greenpix.messenger.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.dto.UserSortListDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.BlacklistUserAccessRestrictionException;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;
import ru.greenpix.messenger.user.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.user.mapper.FilterMapper;
import ru.greenpix.messenger.user.mapper.SortMapper;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.repository.UserRepository;
import ru.greenpix.messenger.user.service.impl.UserServiceImpl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    static SignUpDto SIGN_UP_DTO_TEST = new SignUpDto(
            "", "", "", null, null, null, ""
    );
    static SignInDto SIGN_IN_DTO_TEST = new SignInDto(
            "", ""
    );
    static UserSortListDto USER_SORT_LIST_DTO_TEST = new UserSortListDto(
            null, null, null, null,
            null, null, null, null, null
    );
    static UserFilterListDto USER_FILTER_LIST_DTO_TEST = new UserFilterListDto(
            null, null, null,
            null, null, null, null
    );
    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    static UUID TEST_UUID = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static UserRequestDto USER_REQUEST_DTO_TEST = new UserRequestDto(
            null, null, null, null, null
    );

    @Mock
    Clock clock;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    FriendsClient friendsClient;
    @Mock
    FilterMapper filterMapper;
    @Mock
    SortMapper sortMapper;
    @Mock
    Logger logger;
    @InjectMocks
    UserServiceImpl userService;

    @DisplayName("Проверка успешной регистрации")
    @Test
    void registerTest() {
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(userMapper.toEntity(any(SignUpDto.class))).thenReturn(new User());
        when(userRepository.save(any())).thenReturn(new User());

        assertDoesNotThrow(() -> userService.registerUser(SIGN_UP_DTO_TEST));
        verify(passwordEncoder, only()).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @DisplayName("Проверка регистрации существующего пользователя")
    @Test
    void registerDuplicateUsernameTest() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.registerUser(SIGN_UP_DTO_TEST));
        verify(userRepository, never()).save(any());
    }

    @DisplayName("Проверка успешной аутентификации")
    @Test
    void authenticateTest() {
        User someUser = new User();
        someUser.setHashedPassword("");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(someUser));
        when(passwordEncoder.matches(any(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @DisplayName("Проверка аутентификации несуществующего пользователя")
    @Test
    void authenticateNonExistentUserTest() {
        assertThrows(WrongCredentialsException.class, () -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @DisplayName("Проверка аутентификации с неверным паролем")
    @Test
    void authenticateWrongPasswordTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(WrongCredentialsException.class, () -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @DisplayName("Проверка успешного получения пользователя по логину")
    @Test
    void getUserByUsernameTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.getUser(""));
    }

    @DisplayName("Проверка получения несуществующего пользователя по логину")
    @Test
    void getNonExistentUserByUsernameTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(""));
    }

    @DisplayName("Проверка успешного получения пользователя по ID")
    @Test
    void getUserByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.getUser(TEST_UUID));
    }

    @DisplayName("Проверка получения несуществующего пользователя по ID")
    @Test
    void getNonExistentUserByIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(TEST_UUID));
    }

    @DisplayName("Проверка успешного получения пользователя по ID другим пользователем")
    @Test
    void getRequestedUserByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.getUser(TEST_UUID, TEST_UUID));
    }

    @DisplayName("Проверка получения пользователя по ID другим пользователем, которого заблокировал первый пользователь")
    @Test
    void tryBlockedRequestedUserByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(friendsClient.isBlockedByUser(TEST_UUID, TEST_UUID)).thenReturn(true);

        assertThrows(BlacklistUserAccessRestrictionException.class, () -> userService.getUser(TEST_UUID, TEST_UUID));
    }

    @DisplayName("Проверка получения несуществующего пользователя по ID другим пользователем")
    @Test
    void getRequestedNonExistentUserByIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(TEST_UUID, TEST_UUID));
    }

    @DisplayName("Проверка успешного получения страницы пользователей")
    @Test
    void getUserPageTest() {
        when(sortMapper.toUserSort(any())).thenReturn(Sort.unsorted());
        when(filterMapper.toUserSpecification(any())).thenReturn(Specification.where(null));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<User> actual = userService.getUsers(
                1,
                1,
                USER_SORT_LIST_DTO_TEST,
                USER_FILTER_LIST_DTO_TEST
        );

        assertEquals(Page.empty(), actual);
    }

    @DisplayName("Проверка успешного обновления профиля пользователя")
    @Test
    void updateUserTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());

        assertDoesNotThrow(() -> userService.updateUser(TEST_UUID, USER_REQUEST_DTO_TEST));
        verify(userRepository, times(1)).save(any());
    }
}
