package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Детальная информация о сообщении")
public class MessageDetailsDto implements Serializable {

    @Schema(description = "Идентификатор сообщения")
    private final UUID id;

    @Schema(description = "Дата и время отправки сообщения")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDateTime sendingTimestamp;

    @Schema(description = "Текст сообщения")
    private final String text;

    @Schema(description = "Имя отправителя")
    private final String senderName;

    @Schema(description = "Идентификатор файла аватарки отправителя")
    private final UUID senderAvatarId;

    @Schema(description = "Приложения")
    private final List<AttachmentDto> attachments;

}
