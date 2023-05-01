package ru.greenpix.messenger.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.jwt.model.JwtUser;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "Сообщения")
@RestController
@RequiredArgsConstructor
@Validated
public class MessageController {

    @Operation(summary = "Отправка сообщения в ЛС")
    @GetMapping("users/{userId}/messages")
    public void searchMessages(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId,

            @RequestBody
            @Valid
            SendingMessageDto messageDto
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Список сообщений в порядке убывания по дате отправки")
    @GetMapping("chats/{chatId}/messages")
    public List<MessageDetailsDto> getChatMessages(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Отправка сообщения в чат")
    @PostMapping("chats/{chatId}/messages")
    public void sendChatMessage(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId,

            @RequestBody
            @Valid
            SendingMessageDto messageDto
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

    @Operation(summary = "Поиск сообщений")
    @GetMapping("chats/messages")
    public List<MessageDto> searchMessages(
            @AuthenticationPrincipal
            JwtUser jwtUser
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }

}
