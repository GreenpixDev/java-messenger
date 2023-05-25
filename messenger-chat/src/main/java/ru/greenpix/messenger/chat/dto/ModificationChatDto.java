package ru.greenpix.messenger.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class ModificationChatDto implements Serializable {

    @Schema(description = "Название чата")
    @Size(max = 255)
    private final String name;

    @Schema(description = "Идентификаторы участников чата")
    private final List<UUID> members;

}
