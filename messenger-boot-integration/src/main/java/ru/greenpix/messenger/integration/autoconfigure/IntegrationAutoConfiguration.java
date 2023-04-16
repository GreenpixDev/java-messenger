package ru.greenpix.messenger.integration.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.greenpix.messenger.integration.configuration.ApiKeyConfiguration;
import ru.greenpix.messenger.integration.configuration.IntegrationConfiguration;

@Configuration
@Import({IntegrationConfiguration.class, ApiKeyConfiguration.class})
public class IntegrationAutoConfiguration {
}
