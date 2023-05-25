package ru.greenpix.messenger.chat.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;

import java.util.UUID;

/**
 * Сервис для взаимодействия с чатами
 */
public interface ChatService {

    /**
     * Метод получения всех доступных чатов конкретному пользователю
     * @param userId идентификатор пользователей
     * @param page номер страницы
     * @param size размер страницы
     * @param nameFilter фильтр по названию чата
     * @return страница с DTO чата
     */
    @NotNull
    Page<ChatDto> getAccessibleChat(@NotNull UUID userId, int page, int size, String nameFilter);

    /**
     * Метод получения информации о чате
     * @param requesterId идентификатор пользователя, который хочет получить эту информацию
     * @param chatId идентификатор чата
     * @return DTO чата с детальной информацией
     */
    @NotNull
    ChatDetailsDto getChat(@NotNull UUID requesterId, @NotNull UUID chatId);

    /**
     * Метод создания нового группового чата
     * @param creatorId идентификатор пользователя, который создаёт чат
     * @param modificationChatDto DTO с информацией о чате, который нужно создать
     */
    void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto);

    /**
     * Метод обновления группового чата
     * @param creatorId идентификатор пользователя, который создал чат
     * @param chatId идентификатор чата
     * @param modificationChatDto DTO с информацией о чате, который нужно обновить
     */
    void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId, @NotNull ModificationChatDto modificationChatDto);

}
