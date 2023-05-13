package ru.greenpix.messenger.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.entity.NotificationStatus;
import ru.greenpix.messenger.notification.entity.Notification_;
import ru.greenpix.messenger.notification.repository.NotificationRepository;
import ru.greenpix.messenger.notification.service.NotificationService;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public @NotNull Page<Notification> getNotifications(@NotNull UUID userId, int page, int size, @NotNull NotificationFilterListDto filters) {
        return notificationRepository.findAll(PageRequest.of(page, size, Sort.by(
                Sort.Direction.DESC, Notification_.DELIVERY_TIME
        )));
    }

    @Override
    public int getUnreadNotificationCount(@NotNull UUID userId) {
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.UNREAD);
    }

    @Override
    public void updateNotificationStatus(@NotNull UUID userId, Collection<UUID> notificationsIds, NotificationStatus status) {
        notificationRepository.updateAllStatusByIds(userId, status, notificationsIds);
    }
}
