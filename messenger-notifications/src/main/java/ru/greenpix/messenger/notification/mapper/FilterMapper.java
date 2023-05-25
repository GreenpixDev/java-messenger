package ru.greenpix.messenger.notification.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.entity.Notification;

public interface FilterMapper {

    @NotNull
    Specification<Notification> toNotificationSpecification(NotificationFilterListDto dto);

}
