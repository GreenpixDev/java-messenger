package ru.greenpix.messenger.notification.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.dto.NotificationStatus;
import ru.greenpix.messenger.notification.entity.Notification;

import java.util.Collection;
import java.util.UUID;

public interface NotificationService {

    /**
     * Получить страницу уведомлений пользователя
     * @param userId ID пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @param filters фильтры
     * @return страница сущностей уведомлений
     */
    @NotNull
    Page<Notification> getNotifications(@NotNull UUID userId, int page, int size, @NotNull NotificationFilterListDto filters);

    /**
     * Получить количество непрочитанных уведомлений у пользователя
     * @param userId ID пользователя
     * @return количество непрочитанных уведомлений
     */
    int getUnreadNotificationCount(@NotNull UUID userId);

    /**
     * Метод обновления статуса уведомлений.
     * Статус уведомления может быть: прочитано, не прочитано.
     * @param userId ID пользователя
     * @param notificationsIds идентификаторы уведомлений
     * @param status статус уведомления
     */
    void updateNotificationStatus(@NotNull UUID userId, Collection<UUID> notificationsIds, NotificationStatus status);

    /**
     * Сохраняет полученное из брокера сообщения уведомление в базу данных
     * @param dto DTO уведомления из брокера сообщений
     */
    void saveNotification(@NotNull NotificationAmqpDto dto);

}
