package ru.greenpix.messenger.user.settings;

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
public class JwtSettings {

    @NotBlank
    private final String secret;

    @PositiveOrZero
    private final Long expirationMinutes;

}
