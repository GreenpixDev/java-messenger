package ru.greenpix.messenger.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.auth.controller.dto.SignInRequestDto;
import ru.greenpix.messenger.auth.controller.dto.UserResponseDto;

@RestController
@RequestMapping("me")
public class MeController {

    @GetMapping
    public UserResponseDto getUser(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PutMapping
    public UserResponseDto updateUser(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
