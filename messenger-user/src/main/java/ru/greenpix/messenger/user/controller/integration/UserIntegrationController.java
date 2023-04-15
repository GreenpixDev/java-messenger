package ru.greenpix.messenger.user.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.user.service.UserService;

import java.util.UUID;

@Tag(name = "Интеграционный контроллер пользователей")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserIntegrationController {

    private final UserService userService;

    @Operation(summary = "Получить ФИО пользователя")
    @GetMapping("{userId}/full-name")
    public String getUserFullName(
            @PathVariable UUID userId
    ) {
        return userService.getUser(userId).getFullName();
    }

}
