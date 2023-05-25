package ru.greenpix.messenger.notification.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.greenpix.messenger.amqp.dto.NotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность уведомлений
 */
@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification {

    /**
     * Идентификатор уведомления
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Идентификатор пользователя, которому адресовано уведомление
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Временная отметка доставки уведомления пользователю
     */
    @Column(name = "delivery_timestamp", nullable = false)
    private LocalDateTime deliveryTimestamp;

    /**
     * Тип уведомления
     */
    @Column(name = "type", nullable = false)
    private NotificationType type;

    /**
     * Временная отметка прочтения уведомления пользователем
     */
    @Column(name = "reading_timestamp")
    private LocalDateTime readingTimestamp;

    /**
     * Текст (сообщение) уведомления
     */
    @Column(name = "text", nullable = false)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification that = (Notification) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}