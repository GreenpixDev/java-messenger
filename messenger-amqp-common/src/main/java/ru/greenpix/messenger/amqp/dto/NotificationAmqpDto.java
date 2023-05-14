package ru.greenpix.messenger.amqp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationAmqpDto {

    private final UUID userId;
    private final NotificationType type;
    private final String text;

}
