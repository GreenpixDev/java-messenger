package ru.greenpix.messenger.chat.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;

import java.util.UUID;

public interface ChatService {

    @NotNull
    Page<ChatDto> getAccessibleChat(@NotNull UUID userId, int page, int size, String nameFilter);

    @NotNull
    ChatDetailsDto getChat(@NotNull UUID requesterId, @NotNull UUID chatId);

    void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto);

    void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId, @NotNull ModificationChatDto modificationChatDto);

}
