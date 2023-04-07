package ru.greenpix.messenger.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.UserNotFoundException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.repository.UserRepository;
import ru.greenpix.messenger.user.service.impl.UserServiceImpl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    static SignUpDto SIGN_UP_DTO_TEST = new SignUpDto(
            "", "", "", null, null, null, ""
    );
    static SignInDto SIGN_IN_DTO_TEST = new SignInDto(
            "", ""
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

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void registerTest() {
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        when(userMapper.toEntity(any(SignUpDto.class))).thenReturn(new User());

        assertDoesNotThrow(() -> userService.registerUser(SIGN_UP_DTO_TEST));
        verify(passwordEncoder, only()).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerDuplicateUsernameTest() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.registerUser(SIGN_UP_DTO_TEST));
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticateTest() {
        User someUser = new User();
        someUser.setHashedPassword("");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(someUser));
        when(passwordEncoder.matches(any(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @Test
    void authenticateNonExistentUserTest() {
        assertThrows(WrongCredentialsException.class, () -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @Test
    void authenticateWrongPasswordTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(WrongCredentialsException.class, () -> userService.authenticateUser(SIGN_IN_DTO_TEST));
    }

    @Test
    void getUserByUsernameTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.getUser(""));
    }

    @Test
    void getNonExistentUserByUsernameTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(""));
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.getUser(TEST_UUID));
    }

    @Test
    void getNonExistentUserByIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser(TEST_UUID));
    }

    @Test
    void updateUserTest() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.updateUser(TEST_UUID, USER_REQUEST_DTO_TEST));
        verify(userRepository, times(1)).save(any());
    }
}
