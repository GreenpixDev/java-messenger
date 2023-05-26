package ru.greenpix.messenger.storage.service;

import java.util.UUID;

/**
 * Сервис для взаимодействия с файловым хранилищем
 */
public interface FileStorageService {

    /**
     * Метод загрузки файла в хранилище
     * @param content контент файла в байтах
     * @return идентификатор файла в хранилище
     */
    UUID uploadFile(byte[] content);

    /**
     * Метод скачивания файла с хранилища
     * @param identifier идентификатор файла в хранилище
     * @return массив байтов файла
     */
    byte[] downloadFile(UUID identifier);

}
