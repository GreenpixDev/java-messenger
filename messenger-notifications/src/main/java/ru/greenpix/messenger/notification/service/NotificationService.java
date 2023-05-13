package ru.greenpix.messenger.notification.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.entity.NotificationStatus;

import java.util.Collection;
import java.util.UUID;

public interface NotificationService {

    @NotNull
    Page<Notification> getNotifications(@NotNull UUID userId, int page, int size, @NotNull NotificationFilterListDto filters);

    int getUnreadNotificationCount(@NotNull UUID userId);

    void updateNotificationStatus(@NotNull UUID userId, Collection<UUID> notificationsIds, NotificationStatus status);

}
