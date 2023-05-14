package ru.greenpix.messenger.friends.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "notification")
@ConstructorBinding
@Validated
public class NotificationSettings {

    /**
     * Текст уведомления о добавлении в друзья
     */
    private final String addFriend;

    /**
     * Текст уведомления об удалении из друзей
     */
    private final String removeFriend;

    /**
     * Текст уведомления о добавлении в черный список
     */
    private final String block;

    /**
     * Текст уведомления об удалении из черного списка
     */
    private final String unblock;

}
