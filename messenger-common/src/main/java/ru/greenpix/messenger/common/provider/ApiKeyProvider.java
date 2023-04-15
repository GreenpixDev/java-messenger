package ru.greenpix.messenger.common.provider;

public interface ApiKeyProvider {

    /**
     * API ключ для интеграционных межсерверных запросов
     */
    String getApiKey();

}
