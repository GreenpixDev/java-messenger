package ru.greenpix.messenger.chat.integration.storage.client;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface FileStorageClient {

    Optional<UUID> uploadFile(MultipartFile file);

}
