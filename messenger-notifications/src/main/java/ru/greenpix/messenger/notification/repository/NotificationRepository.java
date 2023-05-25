package ru.greenpix.messenger.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.greenpix.messenger.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {

    @Query("select count(n) from Notification n where n.userId = :userId and n.readingTimestamp is null")
    int countUnreadByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("update Notification n set n.readingTimestamp = :readingTimestamp where n.userId = :userId and n.id in :ids")
    void updateAllReadingTimestampByIds(UUID userId, LocalDateTime readingTimestamp, Collection<UUID> ids);

}