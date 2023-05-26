package ru.greenpix.messenger.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.service.ChatService;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.jwt.model.JwtUser;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Tag(name = "Чаты")
@RestController
@RequestMapping("chats")
@RequiredArgsConstructor
@Validated
public class ChatController {

    private final ChatService chatService;
    private final PageMapper pageMapper;

    @Operation(summary = "Получение списка переписок")
    @GetMapping
    public PageDto<ChatDto> getChatList(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @Positive
            @RequestParam(name = "page", defaultValue = "1")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size", defaultValue = "50")
            int pageSize,

            @RequestParam(defaultValue = "")
            String nameFilter
    ) {
        return pageMapper.toDto(chatService.getAccessibleChat(jwtUser.getId(), pageNumber - 1, pageSize, nameFilter));
    }

    @Operation(summary = "Получение информации о чате")
    @GetMapping("{chatId}")
    public ChatDetailsDto getChatDetails(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId
    ) {
        return chatService.getChat(jwtUser.getId(), chatId);
    }

    @Operation(summary = "Создать чат")
    @PostMapping
    public void createChat(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @RequestPart(name = "data")
            @Valid
            ModificationChatDto chatDto,

            @RequestPart(required = false, name = "avatar")
            MultipartFile avatar
    ) {
        chatService.createChat(jwtUser.getId(), chatDto, avatar);
    }

    @Operation(summary = "Изменить чат")
    @PutMapping("{chatId}")
    public void updateChat(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId,

            @RequestPart(name = "data")
            @Valid
            ModificationChatDto chatDto,

            @RequestPart(required = false, name = "avatar")
            MultipartFile avatar
    ) {
        chatService.updateChat(jwtUser.getId(), chatId, chatDto, avatar);
    }

}
