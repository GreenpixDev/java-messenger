package ru.greenpix.messenger.chat.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    @NotNull
    Page<MessageDto> getMessages(@NotNull UUID userId, int page, int size, String textFilter);

    @NotNull
    List<MessageDetailsDto> getChatMessages(@NotNull UUID requesterId, @NotNull UUID chatId);

    void sendPrivateMessage(@NotNull UUID senderId, @NotNull UUID receiverId,
                            @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files);

    void sendGroupMessage(@NotNull UUID senderId, @NotNull UUID chatId,
                          @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files);

}
