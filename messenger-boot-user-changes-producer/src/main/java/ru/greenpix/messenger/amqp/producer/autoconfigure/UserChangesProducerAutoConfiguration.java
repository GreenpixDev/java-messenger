package ru.greenpix.messenger.amqp.producer.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.greenpix.messenger.amqp.producer.configuration.UserChangesProducerConfiguration;

@Configuration
@Import({UserChangesProducerConfiguration.class})
public class UserChangesProducerAutoConfiguration {
}
