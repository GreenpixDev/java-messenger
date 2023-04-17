package ru.greenpix.messenger.jwt.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.jwt.manager.impl.JwtManagerImpl;
import ru.greenpix.messenger.jwt.security.JwtAuthenticationConverter;
import ru.greenpix.messenger.jwt.settings.SecurityJwtSettings;

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
    public JwtAuthenticationConverter jwtAuthenticationConverter(JwtManager manager) {
        return new JwtAuthenticationConverter(manager);
    }

}
