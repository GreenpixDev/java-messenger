package ru.greenpix.messenger.auth.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "security", name = "password-encoder", havingValue = "raw")
public class RawPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(@NotNull CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(@NotNull CharSequence rawPassword, @NotNull String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
