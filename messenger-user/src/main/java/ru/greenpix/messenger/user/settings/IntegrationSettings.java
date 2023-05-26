package ru.greenpix.messenger.user.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "integration")
@ConstructorBinding
@Validated
public class IntegrationSettings {

    /**
     * URL микросервиса "Друзья"
     */
    @NotBlank
    private final String friendsServiceUrl;

    /**
     * URL микросервиса "Файловое хранилище"
     */
    @NotBlank
    private final String fileStorageServiceUrl;

}
