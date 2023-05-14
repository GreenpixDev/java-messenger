package ru.greenpix.messenger.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.dto.NotificationStatus;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.entity.Notification_;
import ru.greenpix.messenger.notification.mapper.NotificationMapper;
import ru.greenpix.messenger.notification.repository.NotificationRepository;
import ru.greenpix.messenger.notification.service.NotificationService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final Logger logger;

    @Override
    public @NotNull Page<Notification> getNotifications(@NotNull UUID userId, int page, int size, @NotNull NotificationFilterListDto filters) {
        logger.trace("Getting notification list (page={}, size={}, filters={}) for user {}", page, size, filters, userId);
        return notificationRepository.findAll(PageRequest.of(page, size, Sort.by(
                Sort.Direction.DESC, Notification_.DELIVERY_TIMESTAMP
        )));
    }

    @Override
    public int getUnreadNotificationCount(@NotNull UUID userId) {
        logger.trace("Getting count of unread notifications for user {}", userId);
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void updateNotificationStatus(@NotNull UUID userId, Collection<UUID> notificationsIds, NotificationStatus status) {
        LocalDateTime readingTimestamp = status == NotificationStatus.READ
                ? LocalDateTime.now()
                : null;
        notificationRepository.updateAllReadingTimestampByIds(userId, readingTimestamp, notificationsIds);
        logger.debug("Status of notifications {} changed to {} for user {}", userId, status, userId);
    }

    @Override
    public void saveNotification(@NotNull NotificationAmqpDto dto) {
        logger.debug("Receive message {}", dto);
        Notification notification = notificationMapper.toEntity(dto);
        notification.setDeliveryTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
        logger.info("Save notification {} for user {}", notification.getType(), notification.getUserId());
    }
}
