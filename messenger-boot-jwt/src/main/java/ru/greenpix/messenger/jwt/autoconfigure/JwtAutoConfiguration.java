package ru.greenpix.messenger.jwt.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.greenpix.messenger.jwt.configuration.JwtConfiguration;

@Configuration
@Import({JwtConfiguration.class})
public class JwtAutoConfiguration {
}
