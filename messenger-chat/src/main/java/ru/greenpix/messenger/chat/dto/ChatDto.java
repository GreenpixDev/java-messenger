package ru.greenpix.messenger.chat.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatDto implements Serializable {

    private final UUID id;

    private final String name;

    private final String lastMessageText;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDateTime lastMessageSendingTimestamp;

    private final UUID lastMessageSenderId;

}
