package ru.greenpix.messenger.chat.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class ModificationChatDto implements Serializable {

    @Size(max = 255)
    private final String name;

    private final List<UUID> members;

}
