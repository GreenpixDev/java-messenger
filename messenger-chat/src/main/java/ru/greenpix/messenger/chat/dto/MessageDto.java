package ru.greenpix.messenger.chat.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto implements Serializable {

    private final UUID chatId;

    private final String chatName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime sendingTimestamp;

    private final String text;

    // TODO вложения

}
