package ru.greenpix.messenger.chat.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.amqp.service.UserChangesService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final UserChangesService userChangesService;

    /**
     * Bean для приёма сообщений из RabbitMQ
     */
    @Bean
    public Consumer<UserChangesAmqpDto> userChangesConsumer() {
        return userChangesService::update;
    }

}
