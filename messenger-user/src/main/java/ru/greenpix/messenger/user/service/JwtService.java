package ru.greenpix.messenger.user.service;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.user.model.JwtUser;

public interface JwtService {

    String generateToken(@NotNull JwtUser user);

    boolean validateToken(@NotNull String token);

    JwtUser parseUser(@NotNull String token);



}
