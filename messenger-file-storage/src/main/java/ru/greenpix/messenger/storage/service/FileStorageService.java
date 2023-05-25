package ru.greenpix.messenger.storage.service;

import java.util.UUID;

public interface FileStorageService {

    UUID uploadFile(byte[] content, String contentType);

    byte[] downloadFile(UUID identifier);

    void deleteFile(UUID identifier);

}
