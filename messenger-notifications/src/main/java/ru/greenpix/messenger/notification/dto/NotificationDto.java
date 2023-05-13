package ru.greenpix.messenger.notification.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.greenpix.messenger.notification.entity.NotificationStatus;
import ru.greenpix.messenger.notification.entity.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDto {

    private final UUID id;

    private final NotificationType type;

    private final String text;

    private final NotificationStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDateTime receiveTimestamp;

}
