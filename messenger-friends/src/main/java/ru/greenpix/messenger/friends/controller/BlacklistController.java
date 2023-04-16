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
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.friends.dto.BlockedUserDetailsDto;
import ru.greenpix.messenger.friends.dto.BlockedUserDto;
import ru.greenpix.messenger.friends.dto.BlockedUserSearchDto;
import ru.greenpix.messenger.friends.entity.BlockedUser;
import ru.greenpix.messenger.friends.mapper.BlockedUserMapper;
import ru.greenpix.messenger.friends.service.BlacklistService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Tag(name = "Черный список")
@RestController
@RequestMapping("users/me/blacklist")
@RequiredArgsConstructor
@Validated
public class BlacklistController {

    private final BlacklistService blacklistService;
    private final BlockedUserMapper blockedUserMapper;
    private final PageMapper pageMapper;

    @Operation(summary = "Получить черный список")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public PageDto<BlockedUserDto> getBlacklist(
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
        Page<BlockedUser> page = blacklistService.getBlockedUserPage(
                jwtUser.getId(),
                pageNumber - 1,
                pageSize,
                fullNameFilter
        );
        return pageMapper.toDto(page.map(blockedUserMapper::toDto));
    }

    @Operation(summary = "Получить информацию о заблокированном пользователе")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}")
    public BlockedUserDetailsDto getBlockedUserDetails(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        return blockedUserMapper.toDetailsDto(blacklistService.getBlockedUser(jwtUser.getId(), userId));
    }

    @Operation(summary = "Добавить пользователя в черный список")
    @ApiResponse(responseCode = "200")
    @PostMapping("{userId}")
    public void addBlockedUser(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        blacklistService.addBlockedUser(jwtUser.getId(), userId);
    }

    @Operation(summary = "Синхронизировать данные")
    @ApiResponse(responseCode = "200")
    @PutMapping
    public void synchronizeBlockedUser(
            @AuthenticationPrincipal
            JwtUser jwtUser
    ) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Убрать пользователя из черного списока")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{userId}")
    public void deleteBlockedUser(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        blacklistService.deleteBlockedUser(jwtUser.getId(), userId);
    }

    @Operation(summary = "Поиск пользователя в черном списке")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public PageDto<BlockedUserDto> searchBlockedUser(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @Positive
            @RequestParam(name = "page")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size")
            int pageSize,

            @ParameterObject
            BlockedUserSearchDto searchDto
    ) {
        Page<BlockedUser> page = blacklistService.getBlockedUserPage(
                jwtUser.getId(),
                pageNumber - 1,
                pageSize,
                searchDto
        );
        return pageMapper.toDto(page.map(blockedUserMapper::toDto));
    }

    @Operation(summary = "Проверить нахождение пользователя в черном списке")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}/status")
    public boolean getStatus(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId
    ) {
        return blacklistService.isBlockedByUser(jwtUser.getId(), userId);
    }
}
