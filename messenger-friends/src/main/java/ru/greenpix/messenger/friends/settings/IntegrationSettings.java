package ru.greenpix.messenger.friends.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;
import ru.greenpix.messenger.common.provider.ApiKeyProvider;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "integration")
@ConstructorBinding
@Validated
public class IntegrationSettings implements ApiKeyProvider {

    /**
     * API ключ для интеграционных межсерверных запросов
     */
    @NotBlank
    private final String apiKey;

}
