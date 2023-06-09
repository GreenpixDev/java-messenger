package ru.greenpix.messenger.integration.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationConverter;
import ru.greenpix.messenger.integration.settings.SecurityApiSettings;

@Configuration
@EnableConfigurationProperties({SecurityApiSettings.class})
public class ApiKeyConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApiKeyAuthenticationConverter apiKeyAuthenticationConverter(SecurityApiSettings securityApiSettings) {
        return new ApiKeyAuthenticationConverter(securityApiSettings.getKey());
    }

}
