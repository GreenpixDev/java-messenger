package ru.greenpix.messenger.friends.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;
import ru.greenpix.messenger.common.provider.JwtSettingsProvider;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@ConfigurationProperties(prefix = "security.jwt")
@ConstructorBinding
@Validated
public class JwtSettings implements JwtSettingsProvider {

    @NotBlank
    private final String secret;

    @PositiveOrZero
    private final long expirationMinutes;

}
