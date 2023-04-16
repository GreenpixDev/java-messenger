package ru.greenpix.messenger.auth.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.auth.security.key.ApiKeyAuthenticationConverter;
import ru.greenpix.messenger.auth.security.key.ApiKeyAuthenticationFilter;
import ru.greenpix.messenger.auth.security.role.SystemRole;
import ru.greenpix.messenger.auth.settings.SecurityApiSettings;

@Configuration
@EnableConfigurationProperties({SecurityApiSettings.class})
public class ApiKeyConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApiKeyAuthenticationConverter apiKeyAuthenticationConverter(SecurityApiSettings securityApiSettings, SystemRole systemRole) {
        return new ApiKeyAuthenticationConverter(securityApiSettings.getKey(), systemRole);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter(ApiKeyAuthenticationConverter converter) {
        return new ApiKeyAuthenticationFilter(converter);
    }

    @Bean
    public SystemRole systemRole() {
        return new SystemRole();
    }

}
