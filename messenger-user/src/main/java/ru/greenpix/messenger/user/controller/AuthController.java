package ru.greenpix.messenger.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.SignUpDto;
import ru.greenpix.messenger.user.dto.UserResponseDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Авторизация")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtManager jwtManager;
    private final UserMapper userMapper;

    @Operation(summary = "Зарегистрировать нового пользователя")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PostMapping("signup")
    public UserResponseDto register(
            @Valid
            @RequestBody
            SignUpDto signUpDto,

            HttpServletResponse response
    ) {
        User user = userService.registerUser(signUpDto);
        String token = jwtManager.generateToken(userMapper.toJwt(user));
        response.addHeader(HttpHeaders.AUTHORIZATION, token);
        return userMapper.toDto(user);
    }

    @Operation(summary = "Аутентифицировать пользователя")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PostMapping("signin")
    public UserResponseDto authenticate(
            @Valid
            @RequestBody
            SignInDto signInDto,

            HttpServletResponse response
    ) {
        User user = userService.authenticateUser(signInDto);
        String token = jwtManager.generateToken(userMapper.toJwt(user));
        response.addHeader(HttpHeaders.AUTHORIZATION, token);
        return userMapper.toDto(user);
    }

}
