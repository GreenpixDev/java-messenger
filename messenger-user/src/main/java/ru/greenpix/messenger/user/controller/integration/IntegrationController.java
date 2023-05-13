package ru.greenpix.messenger.user.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.user.dto.integration.UserIntegrationDto;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Интеграционный контроллер пользователей")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class IntegrationController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить ФИО пользователя")
    @GetMapping("{userId}/full-name")
    public String getUserFullName(
            @PathVariable UUID userId
    ) {
        return userService.getUser(userId).getFullName();
    }

    @Operation(summary = "Получить пользователей по их ID")
    @PostMapping("search")
    public List<UserIntegrationDto> getUsers(
            @RequestBody List<UUID> userIds
    ) {
        return userService.getUsers(userIds)
                .stream()
                .map(userMapper::toIntegrationDto)
                .collect(Collectors.toList());
    }
}
