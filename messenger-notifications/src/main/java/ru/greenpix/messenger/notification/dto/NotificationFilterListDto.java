package ru.greenpix.messenger.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.greenpix.messenger.amqp.dto.NotificationType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Текст уведомления")
public class NotificationFilterListDto implements Serializable {

    @Schema(description = "От каких даты и времени получения")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDate filterTimestampFrom;

    @Schema(description = "До каких даты и времени получения")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDate filterTimestampTo;

    @Schema(description = "По тексту уведомления: нечеткий (по подстроке) и регистро-независимый поиск")
    private final String filterText;

    @Schema(description = "По типу уведомления: множественный выбор, когда юзер в UI выбирает несколько значений из списка")
    private final List<NotificationType> filterType;

}
