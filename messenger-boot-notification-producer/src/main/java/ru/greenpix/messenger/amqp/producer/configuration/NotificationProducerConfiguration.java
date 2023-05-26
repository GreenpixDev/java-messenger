package ru.greenpix.messenger.amqp.producer.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;
import ru.greenpix.messenger.amqp.producer.producer.impl.NotificationProducerImpl;
import ru.greenpix.messenger.amqp.producer.settings.NotificationProducerSettings;

@Configuration
@EnableConfigurationProperties({NotificationProducerSettings.class})
public class NotificationProducerConfiguration {

    @Bean
    public NotificationProducer notificationProducer(
            NotificationProducerSettings settings,
            StreamBridge streamBridge
    ) {
        return new NotificationProducerImpl(streamBridge, settings.getTopic());
    }

}
