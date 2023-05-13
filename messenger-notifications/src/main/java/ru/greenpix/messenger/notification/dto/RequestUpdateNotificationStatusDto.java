package ru.greenpix.messenger.notification.dto;

import lombok.Data;
import ru.greenpix.messenger.notification.entity.NotificationStatus;

import java.util.List;
import java.util.UUID;

@Data
public class RequestUpdateNotificationStatusDto {

    private final NotificationStatus status;
    private final List<UUID> notificationIds;

}
