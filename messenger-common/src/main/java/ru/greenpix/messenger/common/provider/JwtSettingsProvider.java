package ru.greenpix.messenger.common.provider;

public interface JwtSettingsProvider {

    String getSecret();

    long getExpirationMinutes();

}
