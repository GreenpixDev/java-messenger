package ru.greenpix.messenger.chat.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
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
     * @param avatar файл аватарки чата
     */
    void createChat(@NotNull UUID creatorId, @NotNull ModificationChatDto modificationChatDto, @Nullable MultipartFile avatar);

    /**
     * Метод обновления группового чата
     * @param creatorId идентификатор пользователя, который создал чат
     * @param chatId идентификатор чата
     * @param modificationChatDto DTO с информацией о чате, который нужно обновить
     * @param avatar новый файл аватарки чата
     */
    void updateChat(@NotNull UUID creatorId, @NotNull UUID chatId, @NotNull ModificationChatDto modificationChatDto, @Nullable MultipartFile avatar);

}
