package ru.greenpix.messenger.notification.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.notification.service.NotificationService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final NotificationService notificationService;

    /**
     * Bean для приёма уведомлений из RabbitMQ
     */
    @Bean
    public Consumer<NotificationAmqpDto> notificationConsumer() {
        return notificationService::saveNotification;
    }

}
