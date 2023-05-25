package ru.greenpix.messenger.user.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "amqp.producer.user-changes")
@ConstructorBinding
@Validated
public class AmqpProducerSettings {

    @NotBlank
    public final String topic;

}