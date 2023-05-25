package ru.greenpix.messenger.chat.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления сообщениями
 */
public interface MessageService {

    /**
     * Метод получения сообщений у пользователя
     * @param userId идентификатор пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param textFilter фильтр по тексту сообщений (или по названию вложений)
     * @return страницу с DTO сообщений
     */
    @NotNull
    Page<MessageDto> getMessages(@NotNull UUID userId, int page, int size, String textFilter);

    /**
     * Метод получения сообщений в чате
     * @param requesterId идентификатор пользователя, который хочет получить сообщения
     * @param chatId идентификатор чата, в котором нужно узнать сообщения
     * @return список DTO сообщений с детальной информацией
     */
    @NotNull
    List<MessageDetailsDto> getChatMessages(@NotNull UUID requesterId, @NotNull UUID chatId);

    /**
     * Метод отправки личного сообщения пользователю
     * @param senderId идентификатор пользователя-отправителя
     * @param receiverId идентификатор пользователя-получателя
     * @param dto DTO с информацией об сообщении, которое нужно отправить
     * @param files приложения в виде файлов
     */
    void sendPrivateMessage(@NotNull UUID senderId, @NotNull UUID receiverId,
                            @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files);

    /**
     * Метод отправки сообщения в групповой чат
     * @param senderId идентификатор пользователя-отправителя
     * @param chatId идентификатор группового чата
     * @param dto DTO с информацией об сообщении, которое нужно отправить
     * @param files приложения в виде файлов
     */
    void sendGroupMessage(@NotNull UUID senderId, @NotNull UUID chatId,
                          @NotNull SendingMessageDto dto, @NotNull MultipartFile[] files);

}
