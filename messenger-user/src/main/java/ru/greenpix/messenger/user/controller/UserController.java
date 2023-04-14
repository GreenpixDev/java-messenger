package ru.greenpix.messenger.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.user.dto.UserResponseDto;
import ru.greenpix.messenger.user.dto.UserSortCriterion;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Пользователи")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // TODO
    @Operation(summary = "Получить список пользователей")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @GetMapping
    public List<UserResponseDto> getUserList(
            @RequestParam("page") int page,
            @RequestParam("count") int count,
            @RequestParam("sort") UserSortCriterion sortCriteria,
            @RequestParam(value = "desc", defaultValue = "false") boolean descending
    ) {
        return userService.getUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить информацию о пользователе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @GetMapping("{userId}")
    public UserResponseDto getUserProfile(
            @PathVariable UUID userId
    ) {
        return userMapper.toDto(userService.getUser(userId));
    }

}
