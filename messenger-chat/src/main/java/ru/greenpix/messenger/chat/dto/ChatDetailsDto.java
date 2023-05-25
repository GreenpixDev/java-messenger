package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Детальная информация о чате")
public class ChatDetailsDto implements Serializable {

    @Schema(description = "Название чата")
    private final String name;

    @Schema(description = "Идентификатор аватарки чата из файлового хранилища")
    private final UUID avatarId;

    @Schema(description = "Идентификатор администратора чата из микросервиса пользователей")
    private final UUID adminUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime creationTimestamp;

}
