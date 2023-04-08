package ru.greenpix.messenger.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Черный список")
@RestController
@RequestMapping("users/me/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    // TODO
    @Operation(summary = "Получить черный список")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public void getBlacklist() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Получить информацию о заблокированном пользователе")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}")
    public void getBlockedUserDetails(
            @PathVariable UUID userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Добавить пользователя в черный список")
    @ApiResponse(responseCode = "200")
    @PostMapping("{userId}")
    public void addBlockedUser(
            @PathVariable UUID userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // TODO
    @Operation(summary = "Синхронизировать данные")
    @ApiResponse(responseCode = "200")
    @PutMapping
    public void synchronizeBlockedUser() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Убрать пользователя из черного списока")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{userId}")
    public void deleteBlockedUser(
            @PathVariable UUID userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // TODO
    @Operation(summary = "Поиск пользователя в черном списке")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public void searchBlockedUser() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Проверить нахождение пользователя в черном списке")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}/status")
    public void getStatus(
            @PathVariable UUID userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
