package ru.greenpix.messenger.auth.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.greenpix.messenger.auth.configuration.JwtConfiguration;

@Configuration
@Import({JwtConfiguration.class})
@EnableWebSecurity
public class AuthAutoConfiguration {
}
