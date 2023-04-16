package ru.greenpix.messenger.auth.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@ConfigurationProperties(prefix = "security.jwt")
@ConstructorBinding
@Validated
public class SecurityJwtSettings {

    /**
     * Секретный ключ для подписи JWT токена
     */
    @NotBlank
    private final String secret;

    /**
     * Срок жизни JWT токена в минутах
     */
    @PositiveOrZero
    private final long expirationMinutes;

}
