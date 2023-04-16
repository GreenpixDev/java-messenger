package ru.greenpix.messenger.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.auth.model.JwtUser;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.dto.UserResponseDto;
import ru.greenpix.messenger.user.dto.UserSortListDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.mapper.UserMapper;
import ru.greenpix.messenger.user.service.UserService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Tag(name = "Пользователи")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;

    @Operation(summary = "Получить список пользователей")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @GetMapping
    public PageDto<UserResponseDto> getUserList(
            @Positive
            @RequestParam(name = "page")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size")
            int pageSize,

            @ParameterObject
            UserSortListDto sorts,

            @ParameterObject
            UserFilterListDto filters
    ) {
        Page<User> page = userService.getUsers(pageNumber - 1, pageSize, sorts, filters);
        return pageMapper.toDto(page.map(userMapper::toDto));
    }

    @Operation(summary = "Получить информацию о пользователе")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @GetMapping("{userId}")
    public UserResponseDto getUserProfile(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        return userMapper.toDto(userService.getUser(jwtUser.getId(), userId));
    }

}
