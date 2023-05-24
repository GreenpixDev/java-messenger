package ru.greenpix.messenger.storage.service;

import java.util.UUID;

public interface FileStorageService {

    UUID uploadFile(byte[] content);

    byte[] downloadFile(UUID identifier);

    void deleteFile(UUID identifier);

}
