package ru.greenpix.messenger.auth.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.auth.manager.JwtManager;
import ru.greenpix.messenger.auth.manager.impl.JwtManagerImpl;
import ru.greenpix.messenger.auth.role.UserRole;
import ru.greenpix.messenger.auth.security.JwtAuthenticationConverter;
import ru.greenpix.messenger.auth.settings.SecurityJwtSettings;

import java.time.Clock;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties({SecurityJwtSettings.class})
public class JwtConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtManager jwtManager(SecurityJwtSettings settings, Optional<Clock> clock) {
        return new JwtManagerImpl(settings.getSecret(), settings.getExpirationMinutes(), clock.orElseGet(Clock::systemDefaultZone));
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter(JwtManager manager, UserRole userRole) {
        return new JwtAuthenticationConverter(manager, userRole);
    }

    @Bean
    public UserRole userRole() {
        return new UserRole();
    }
}
