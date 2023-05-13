package ru.greenpix.messenger.notification.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.greenpix.messenger.notification.entity.NotificationType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class NotificationFilterListDto implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDate filterTimestampFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private final LocalDate filterTimestampTo;

    private final String filterText;

    private final List<NotificationType> filterType;

}
