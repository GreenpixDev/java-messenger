package ru.greenpix.messenger.jwt.manager;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.jwt.model.JwtUser;

public interface JwtManager {

    @NotNull
    String generateToken(@NotNull JwtUser user);

    boolean validateToken(@NotNull String token);

    @NotNull
    JwtUser parseUser(@NotNull String token);

}
