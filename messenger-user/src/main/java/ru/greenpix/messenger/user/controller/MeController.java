package ru.greenpix.messenger.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.user.dto.SignInDto;
import ru.greenpix.messenger.user.dto.UserRequestDto;
import ru.greenpix.messenger.user.dto.UserResponseDto;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import javax.validation.Valid;

@Tag(name = "Авторизированный пользователь")
@RestController
@RequestMapping("users/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить информацию о себе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @GetMapping
    public UserResponseDto getUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        return userMapper.toDto(userService.getUser(new SignInDto(username, password)));
    }

    @Operation(summary = "Обновить информацию о себе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PutMapping
    public void updateUser(
            @RequestParam String username,
            @RequestParam String password,
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        userService.updateUser(new SignInDto(username, password), userRequestDto);
    }
}
