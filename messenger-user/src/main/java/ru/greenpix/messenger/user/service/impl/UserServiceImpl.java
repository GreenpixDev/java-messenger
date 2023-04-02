package ru.greenpix.messenger.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.exception.DuplicateUsernameException;
import ru.greenpix.messenger.user.exception.WrongCredentialsException;
import ru.greenpix.messenger.user.repository.UserRepository;
import ru.greenpix.messenger.user.security.PasswordEncoder;
import ru.greenpix.messenger.user.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void registerUser(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new DuplicateUsernameException();
        }

        String hashedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(signUpDto.getUsername());
        user.setHashedPassword(hashedPassword);
        user.setFullName(signUpDto.getFullName());
        user.setBirthDate(signUpDto.getBirthDate());
        user.setRegistrationTimestamp(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public User getUser(SignInDto signInDto) {
        User user = userRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(WrongCredentialsException::new);

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getHashedPassword())) {
            throw new WrongCredentialsException();
        }

        return user;
    }

    @Transactional
    @Override
    public void updateUser(SignInDto signInDto, UserRequestDto userRequestDto) {
        User user = getUser(signInDto);
        user.setFullName(userRequestDto.getFullName());
        user.setBirthDate(userRequestDto.getBirthDate());

        userRepository.save(user);
    }
}
