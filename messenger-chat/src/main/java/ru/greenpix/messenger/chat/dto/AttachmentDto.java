package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Информация о вложении")
public class AttachmentDto {

    @Schema(description = "Идентификатор сообщения")
    private final UUID id;

    @Schema(description = "Наименование файла")
    private final String name;

    @Schema(description = "Размер файла")
    private final long size;

}
