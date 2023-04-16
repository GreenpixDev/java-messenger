package ru.greenpix.messenger.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.common.exception.UserNotFoundException;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.BlacklistUserAccessRestrictionException;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;
import ru.greenpix.messenger.user.integration.friends.client.FriendsClient;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.model.UserAttribute;
import ru.greenpix.messenger.user.model.UserFilter;
import ru.greenpix.messenger.user.model.UserSort;
import ru.greenpix.messenger.user.repository.UserRepository;
import ru.greenpix.messenger.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Clock clock;
    private final FriendsClient friendsClient;

    @Transactional
    @Override
    public @NotNull User registerUser(@NotNull SignUpDto signUpDto) {
        log.debug("Trying to register new user '{}'", signUpDto.getUsername());
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            log.debug("User '{}' already exists", signUpDto.getUsername());
            throw new DuplicateUsernameException();
        }

        String hashedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = userMapper.toEntity(signUpDto);
        user.setHashedPassword(hashedPassword);
        user.setRegistrationTimestamp(LocalDateTime.now(clock));

        User newUser = userRepository.save(user);
        log.info("New user '{}' with id {} has been registered", newUser.getUsername(), newUser.getId());
        return newUser;
    }

    @Override
    public @NotNull User authenticateUser(@NotNull SignInDto signInDto) {
        log.debug("Trying to authenticate user '{}'", signInDto.getUsername());
        User user = userRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(WrongCredentialsException::new);

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getHashedPassword())) {
            log.debug("Wrong credentials for user '{}'", signInDto.getUsername());
            throw new WrongCredentialsException();
        }

        log.info("User '{}' with id {} has been authenticated", user.getUsername(), user.getId());
        return user;
    }

    @Override
    public @NotNull Page<User> getUsers(int page, int size, @NotNull List<UserSort> sorts, @NotNull List<UserFilter> filters) {
        log.trace("Getting user list (page={}, size={}, sorts={}, filters={})", page, size, sorts, filters);
        List<Sort.Order> orders = sorts.stream()
                .map(e -> new Sort.Order(
                        e.isDescending() ? Sort.Direction.DESC : Sort.Direction.ASC,
                        e.getAttribute().getAttribute().getName()
                ))
                .collect(Collectors.toList());

        Map<UserAttribute, Object> filterMap = filters.stream()
                .collect(Collectors.toMap(UserFilter::getAttribute, UserFilter::getValue));

        return userRepository.findAllWithFilters(
                PageRequest.of(page, size, Sort.by(orders)),
                filterMap
        );
    }

    @Override
    public @NotNull User getUser(@NotNull String username) {
        log.trace("Getting user details for username '{}'", username);
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public @NotNull User getUser(@NotNull UUID userId) {
        log.trace("Getting user details for id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public @NotNull User getUser(@NotNull UUID requesterId, @NotNull UUID requestedUserId) {
        log.trace("User {} requesting details of user {}", requesterId, requestedUserId);
        User user = getUser(requestedUserId);
        if (friendsClient.isBlockedByUser(requesterId, requestedUserId)) {
            throw new BlacklistUserAccessRestrictionException();
        }
        return user;
    }

    @Transactional
    @Override
    public @NotNull User updateUser(@NotNull UUID userId, @NotNull UserRequestDto userRequestDto) {
        User user = getUser(userId);
        log.debug("Updating profile for user {}", userId);
        user.setFullName(userRequestDto.getFullName());
        user.setBirthDate(userRequestDto.getBirthDate());
        user.setPhone(userRequestDto.getPhone());
        user.setCity(userRequestDto.getCity());
        user.setAvatarId(userRequestDto.getAvatarId());

        User updatedUser = userRepository.save(user);
        log.info("User '{}' with id {} has been authenticated", user.getUsername(), user.getId());
        return updatedUser;
    }
}
