package ru.greenpix.messenger.chat.integration.storage.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.chat.integration.storage.client.FileStorageClient;
import ru.greenpix.messenger.chat.settings.IntegrationSettings;
import ru.greenpix.messenger.chat.util.MultipartInputStreamFileResource;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStorageClientImpl implements FileStorageClient {

    private final RestTemplate restTemplate;
    private final IntegrationSettings integrationSettings;

    @Override
    public Optional<UUID> uploadFile(MultipartFile file) {
        String url = integrationSettings.getFileStorageServiceUrl() + "/api/files";
        try {
            return Optional.ofNullable(restTemplate.postForEntity(
                    URI.create(url),
                    createHttpEntity(file),
                    UUID.class
            ).getBody());
        }
        catch (IOException | HttpClientErrorException ignored) {
            return Optional.empty();
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> createHttpEntity(MultipartFile file) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(body, headers);
    }
}
