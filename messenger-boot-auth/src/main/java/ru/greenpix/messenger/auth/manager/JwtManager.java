package ru.greenpix.messenger.auth.manager;

import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.auth.model.JwtUser;

public interface JwtManager {

    @NotNull
    String generateToken(@NotNull JwtUser user);

    boolean validateToken(@NotNull String token);

    @NotNull
    JwtUser parseUser(@NotNull String token);

}
