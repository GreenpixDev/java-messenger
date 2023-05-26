package ru.greenpix.messenger.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.jwt.model.JwtUser;
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
            @AuthenticationPrincipal
            JwtUser user
    ) {
        return userMapper.toDto(userService.getUser(user.getId()));
    }

    @Operation(summary = "Обновить информацию о себе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PutMapping
    public UserResponseDto updateUser(
            @AuthenticationPrincipal
            JwtUser user,

            @Valid
            @RequestBody
            UserRequestDto userRequestDto
    ) {
        return userMapper.toDto(userService.updateUser(user.getId(), userRequestDto));
    }

    @Operation(summary = "Обновить свою аватарку")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PutMapping("avatar")
    public void updateUserAvatar(
            @AuthenticationPrincipal
            JwtUser user,

            @RequestPart(required = false, name = "avatar")
            MultipartFile avatar
    ) {
        userService.updateUserAvatar(user.getId(), avatar);
    }
}
