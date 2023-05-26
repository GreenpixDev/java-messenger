package ru.greenpix.messenger.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.chat.service.MessageService;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.jwt.model.JwtUser;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

@Tag(name = "Сообщения")
@RestController
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final MessageService messageService;
    private final PageMapper pageMapper;

    @Operation(summary = "Отправка сообщения в ЛС")
    @PostMapping("users/{userId}/messages")
    public void sendPrivateMessages(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID userId,

            @RequestPart(name = "data")
            @Valid
            SendingMessageDto messageDto,

            @RequestPart(required = false, name = "attachment")
            MultipartFile[] attachments
    ) {
        messageService.sendPrivateMessage(jwtUser.getId(), userId, messageDto, attachments);
    }

    @Operation(
            summary = "Просмотр переписки",
            description = "Список сообщений переписки в порядке убывания по дате отправки"
    )
    @GetMapping("chats/{chatId}/messages")
    public List<MessageDetailsDto> getChatMessages(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId
    ) {
        return messageService.getChatMessages(jwtUser.getId(), chatId);
    }

    @Operation(summary = "Отправка сообщения в чат")
    @PostMapping("chats/{chatId}/messages")
    public void sendChatMessage(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @PathVariable
            UUID chatId,

            @RequestPart(name = "data")
            @Valid
            SendingMessageDto messageDto,

            @RequestPart(required = false, name = "attachment")
            MultipartFile[] attachments
    ) {
        messageService.sendGroupMessage(jwtUser.getId(), chatId, messageDto, attachments);
    }

    @Operation(summary = "Поиск сообщений")
    @GetMapping("chats/messages")
    public PageDto<MessageDto> searchMessages(
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
            String textFilter
    ) {
        return pageMapper.toDto(messageService.getMessages(jwtUser.getId(), pageNumber - 1, pageSize, textFilter));
    }

}
