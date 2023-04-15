package ru.greenpix.messenger.user.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.user.dto.UserResponseDto;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import java.util.UUID;

@Tag(name = "Интеграционный контроллер пользователей")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserIntegrationController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить информацию о пользователе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @GetMapping("{userId}")
    public UserResponseDto getUser(
            @PathVariable UUID userId
    ) {
        return userMapper.toDto(userService.getUser(userId));
    }

}
