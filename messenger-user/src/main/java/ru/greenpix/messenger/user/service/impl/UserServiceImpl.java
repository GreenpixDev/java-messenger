package ru.greenpix.messenger.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.UserNotFoundException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.repository.UserRepository;
import ru.greenpix.messenger.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public User registerUser(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new DuplicateUsernameException();
        }

        String hashedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = userMapper.toEntity(signUpDto);
        user.setHashedPassword(hashedPassword);
        user.setRegistrationTimestamp(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(SignInDto signInDto) {
        User user = userRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(WrongCredentialsException::new);

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getHashedPassword())) {
            throw new WrongCredentialsException();
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        return null; // TODO
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public User updateUser(UUID userId, UserRequestDto userRequestDto) {
        User user = getUser(userId);
        user.setFullName(userRequestDto.getFullName());
        user.setBirthDate(userRequestDto.getBirthDate());
        user.setPhone(userRequestDto.getPhone());
        user.setCity(userRequestDto.getCity());
        user.setAvatarId(userRequestDto.getAvatarId());

        return userRepository.save(user);
    }
}
