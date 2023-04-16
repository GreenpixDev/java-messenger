package ru.greenpix.messenger.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.friends.dto.FriendDetailsDto;
import ru.greenpix.messenger.friends.dto.FriendDto;
import ru.greenpix.messenger.friends.dto.FriendSearchDto;
import ru.greenpix.messenger.friends.entity.Friend;
import ru.greenpix.messenger.friends.mapper.FriendMapper;
import ru.greenpix.messenger.friends.service.FriendService;
import ru.greenpix.messenger.jwt.model.JwtUser;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Tag(name = "Друзья")
@RestController
@RequestMapping("users/me/friends")
@RequiredArgsConstructor
@Validated
public class FriendController {

    private final FriendService friendService;
    private final FriendMapper friendMapper;
    private final PageMapper pageMapper;

    @Operation(summary = "Получить список друзей")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public PageDto<FriendDto> getFriendList(
            @AuthenticationPrincipal JwtUser jwtUser,

            @Positive
            @RequestParam(name = "page")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size")
            int pageSize,

            @RequestParam(defaultValue = "")
            String fullNameFilter
    ) {
        Page<Friend> page = friendService.getFriendPage(
                jwtUser.getId(),
                pageNumber - 1,
                pageSize,
                fullNameFilter
        );
        return pageMapper.toDto(page.map(friendMapper::toDto));
    }

    @Operation(summary = "Получить информацию о друге")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}")
    public FriendDetailsDto getFriendDetails(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        return friendMapper.toDetailsDto(friendService.getFriend(jwtUser.getId(), userId));
    }

    @Operation(summary = "Добавить друга")
    @ApiResponse(responseCode = "200")
    @PostMapping("{userId}")
    public void addFriend(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        friendService.addFriend(jwtUser.getId(), userId);
    }

    @Operation(summary = "Синхронизировать данные")
    @ApiResponse(responseCode = "200")
    @PutMapping
    public void synchronizeFriend(
            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Удалить друга")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{userId}")
    public void deleteFriend(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        friendService.deleteFriend(jwtUser.getId(), userId);
    }

    @Operation(summary = "Поиск друга")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public PageDto<FriendDto> searchFriend(
            @AuthenticationPrincipal JwtUser jwtUser,

            @Positive
            @RequestParam(name = "page")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size")
            int pageSize,

            @ParameterObject
            FriendSearchDto searchDto
    ) {
        Page<Friend> page = friendService.getFriendPage(
                jwtUser.getId(),
                pageNumber - 1,
                pageSize,
                searchDto
        );
        return pageMapper.toDto(page.map(friendMapper::toDto));
    }
}
