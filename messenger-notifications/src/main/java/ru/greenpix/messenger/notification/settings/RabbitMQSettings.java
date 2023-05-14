package ru.greenpix.messenger.notification.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "rabbitmq")
@ConstructorBinding
@Validated
public class RabbitMQSettings {

    private final String queue;
    private final String exchange;

}
