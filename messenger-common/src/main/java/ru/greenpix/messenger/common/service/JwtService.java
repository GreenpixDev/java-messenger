package ru.greenpix.messenger.common.service;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.common.model.JwtUser;

public interface JwtService {

    String generateToken(@NotNull JwtUser user);

    boolean validateToken(@NotNull String token);

    JwtUser parseUser(@NotNull String token);



}
