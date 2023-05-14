package ru.greenpix.messenger.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.greenpix.messenger.amqp.dto.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Содержит информацию о уведомлении")
public class NotificationDto {

    @Schema(description = "Идентификатор уведомления")
    private final UUID id;

    @Schema(description = "Это поле означает какое именно уведомление пришло. Например, уведомление о добавлении в друзья")
    private final NotificationType type;

    @Schema(description = "Текст уведомления")
    private final String text;

    @Schema(description = "Статус уведомления (прочитано/не прочитано)")
    private final NotificationStatus status;

    @Schema(description = "Дата и время получения уведомления пользователем")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDateTime receiveTimestamp;

}
