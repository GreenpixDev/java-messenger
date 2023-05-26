package ru.greenpix.messenger.amqp.producer.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.amqp.producer.producer.UserChangesProducer;
import ru.greenpix.messenger.amqp.producer.producer.impl.UserChangesProducerImpl;
import ru.greenpix.messenger.amqp.producer.settings.UserChangesProducerSettings;

@Configuration
@EnableConfigurationProperties({UserChangesProducerSettings.class})
public class UserChangesProducerConfiguration {

    @Bean
    public UserChangesProducer userChangesProducer(
            UserChangesProducerSettings settings,
            StreamBridge streamBridge
    ) {
        return new UserChangesProducerImpl(streamBridge, settings.getTopic());
    }

}
