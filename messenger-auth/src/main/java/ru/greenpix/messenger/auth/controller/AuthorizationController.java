package ru.greenpix.messenger.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.auth.dto.SignUpDto;
import ru.greenpix.messenger.auth.service.UserService;

import javax.validation.Valid;

@Tag(name = "Авторизация")
@RestController
@RequestMapping("authorization")
@RequiredArgsConstructor
public class AuthorizationController {

    private final UserService userService;

    @Operation(summary = "Зарегистрировать нового пользователя")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PostMapping("sign-up")
    public void register(
            @Valid @RequestBody SignUpDto signUpDto
    ) {
        userService.registerUser(signUpDto);
    }

}
