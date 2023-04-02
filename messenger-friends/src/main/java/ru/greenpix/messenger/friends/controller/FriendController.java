package ru.greenpix.messenger.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Друзья")
@RestController
@RequestMapping("friends")
@RequiredArgsConstructor
public class FriendController {

    @Operation(summary = "Получить список друзей")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public void getFriendList(
            @RequestParam("targetUserId") UUID targetUserId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Получить информацию о друге")
    @ApiResponse(responseCode = "200")
    @GetMapping("info")
    public void getFriendDetails(
            @RequestParam("targetUserId") UUID targetUserId,
            @RequestParam("friendUserId") UUID friendUserId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Добавить друга")
    @ApiResponse(responseCode = "200")
    @PostMapping
    public void addFriend() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Синхронизировать данные")
    @ApiResponse(responseCode = "200")
    @PutMapping
    public void synchronizeFriend() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Удалить друга")
    @ApiResponse(responseCode = "200")
    @DeleteMapping
    public void deleteFriend() {
        throw new UnsupportedOperationException("Not implemented");
    }

    // TODO почему POST?
    @Operation(summary = "Поиск друга")
    @ApiResponse(responseCode = "200")
    @PostMapping
    public void searchFriend() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
