package ru.greenpix.messenger.user.security;

import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "security", name = "password-encoder", havingValue = "bcrypt")
public class BCryptPasswordEncoder implements PasswordEncoder {

    private final int rounds;

    public BCryptPasswordEncoder() {
        this(10);
    }

    public BCryptPasswordEncoder(int rounds) {
        this.rounds = rounds;
    }

    @Override
    public String encode(@NotNull CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(rounds));
    }

    @Override
    public boolean matches(@NotNull CharSequence rawPassword, @NotNull String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
