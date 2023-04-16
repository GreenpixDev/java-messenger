package ru.greenpix.messenger.integration.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.greenpix.messenger.integration.configuration.IntegrationConfiguration;

@Configuration
@Import({IntegrationConfiguration.class})
@EnableWebSecurity
public class IntegrationAutoConfiguration {
}
