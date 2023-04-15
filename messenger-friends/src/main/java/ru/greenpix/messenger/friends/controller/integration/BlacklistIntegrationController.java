package ru.greenpix.messenger.friends.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.friends.service.BlacklistService;

import java.util.UUID;

@Tag(name = "Интеграционный контроллер пользователей")
@RestController
@RequestMapping("api/users/{targetUserId}/blacklist")
@RequiredArgsConstructor
public class BlacklistIntegrationController {

    private final BlacklistService blacklistService;

    @Operation(summary = "Проверить нахождение пользователя в черном списке")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}/status")
    public boolean getStatus(
            @PathVariable UUID targetUserId,
            @PathVariable UUID userId
    ) {
        return blacklistService.isBlockedByUser(targetUserId, userId);
    }
}
