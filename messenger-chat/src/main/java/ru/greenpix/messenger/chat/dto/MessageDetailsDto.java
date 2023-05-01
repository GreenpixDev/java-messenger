package ru.greenpix.messenger.chat.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDetailsDto implements Serializable {

    private final UUID id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime sendingTimestamp;

    private final String text;

    private final String senderName;

    private final UUID senderAvatarId;

    // TODO вложения

}
