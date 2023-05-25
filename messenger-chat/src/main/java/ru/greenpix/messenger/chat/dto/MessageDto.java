package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Информация о сообщении")
public class MessageDto implements Serializable {

    @Schema(description = "Идентификатор чата сообщения")
    private final UUID chatId;

    @Schema(description = "Название чата сообщения")
    private final String chatName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Schema(description = "Дата и время отправки сообщения")
    private final LocalDateTime sendingTimestamp;

    @Schema(description = "Текст сообщения")
    private final String text;

    @Schema(description = "Флаг, который указывает, присутствуют ли вложения в сообщении")
    private final boolean hasAttachments;

    @Schema(description = "Наименование вложения")
    private final String attachmentName;

}
