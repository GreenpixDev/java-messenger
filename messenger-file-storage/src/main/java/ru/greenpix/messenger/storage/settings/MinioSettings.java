package ru.greenpix.messenger.storage.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * Файл конфигурации настроек MinIO
 */
@Data
@ConfigurationProperties(prefix = "minio")
@ConstructorBinding
@Validated
public class MinioSettings {

    /**
     * Ключ доступа
     */
    private final String accessKey;

    /**
     * Секретный ключ
     */
    private final String secretKey;

    /**
     * Bucket (ведро)
     */
    private final String bucket;

    /**
     * URL строка к MinIO
     */
    private final String url;

}
