package ru.greenpix.messenger.jwt.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "security.api")
@ConstructorBinding
@Validated
public class SecurityApiSettings {

    /**
     * API ключ для интеграционных межсерверных запросов
     */
    @NotBlank
    private final String key;

}
