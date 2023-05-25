package ru.greenpix.messenger.chat.integration.storage.client;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface FileStorageClient {

    /**
     * Метод загрузки файла в файловое хранилище
     * @param file файл, который надо загрузить
     * @return идентификатор загруженного файла, если он был загружен успешно
     */
    Optional<UUID> uploadFile(MultipartFile file);

}
