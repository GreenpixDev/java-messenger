package ru.greenpix.messenger.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Содержит поля для обновления статуса уведомлений")
public class RequestUpdateNotificationStatusDto {

    @Schema(description = "Статус, на который нужно обновить уведомления")
    private final NotificationStatus status;

    @Schema(description = "Список идентификаторов уведомлений, у которых нужно обновить статус")
    private final List<UUID> notificationIds;

}
