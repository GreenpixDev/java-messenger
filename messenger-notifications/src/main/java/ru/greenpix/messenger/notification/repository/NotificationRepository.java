package ru.greenpix.messenger.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.entity.NotificationStatus;

import java.util.Collection;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    int countByUserIdAndStatus(UUID userId, NotificationStatus status);

    @Modifying
    @Query("update Notification n set n.status = :status where n.userId = :userId and n.id in :ids")
    void updateAllStatusByIds(UUID userId, NotificationStatus status, Collection<UUID> ids);

}