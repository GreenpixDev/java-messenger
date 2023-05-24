package ru.greenpix.messenger.storage.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "minio")
@ConstructorBinding
@Validated
public class MinioSettings {

    private final String accessKey;

    private final String secretKey;

    private final String bucket;

    private final String url;

}
