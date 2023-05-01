package ru.greenpix.messenger.chat.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatDetailsDto implements Serializable {

    private final String name;

    private final UUID avatarId;

    private final UUID adminUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime creationTimestamp;

}
