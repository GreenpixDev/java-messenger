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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.jwt.model.JwtUser;

import javax.validation.Valid;
import java.util.UUID;

@Tag(name = "Чаты")
@RestController
@RequestMapping("chats")
@RequiredArgsConstructor
@Validated
public class ChatController {

    @Operation(summary = "Получение списка переписок")
    @GetMapping
    public PageDto<ChatDto> getChatList(
            @AuthenticationPrincipal
            JwtUser jwtUser
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Получение информации о чате")
    @GetMapping("{chatId}")
    public ChatDetailsDto getChatDetails(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Создать чат")
    @PostMapping
    public void createChat(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @RequestBody
            @Valid
            ModificationChatDto chatDto
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Изменить чат")
    @PutMapping("{chatId}")
    public void updateChat(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId,

            @RequestBody
            @Valid
            ModificationChatDto chatDto
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

}
