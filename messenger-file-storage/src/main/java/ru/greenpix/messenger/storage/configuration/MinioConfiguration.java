package ru.greenpix.messenger.storage.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.greenpix.messenger.storage.settings.MinioSettings;

@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioSettings settings;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(settings.getUrl())
                .credentials(settings.getAccessKey(), settings.getSecretKey())
                .build();
    }
}
