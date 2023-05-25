package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Информация о чате")
public class ChatDto implements Serializable {

    @Schema(description = "Идентификатор чата")
    private final UUID id;

    @Schema(description = "Название чата")
    private final String name;

    @Schema(description = "Текст последнего сообщения")
    private final String lastMessageText;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Schema(description = "Дата и время отправки последнего сообщения")
    private final LocalDateTime lastMessageSendingTimestamp;

    @Schema(description = "Идентификатор отправителя последнего сообщения")
    private final UUID lastMessageSenderId;

}
