package ru.greenpix.messenger.chat.settings;

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
     * Текст уведомления о новом сообщении
     */
    private final String newPrivateMessage;

}
