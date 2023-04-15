package ru.greenpix.messenger.common.provider;

public interface JwtSettingsProvider {

    /**
     * Секретный ключ для подписи JWT токена
     */
    String getSecret();

    /**
     * Срок жизни JWT токена в минутах
     */
    long getExpirationMinutes();

}
