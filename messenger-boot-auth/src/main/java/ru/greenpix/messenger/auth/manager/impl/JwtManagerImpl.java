package ru.greenpix.messenger.auth.manager.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import ru.greenpix.messenger.auth.manager.JwtManager;
import ru.greenpix.messenger.auth.model.JwtUser;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class JwtManagerImpl implements JwtManager {

    private static final String USERNAME_CLAIM = "username";

    private final long expirationMinutes;
    private final Clock clock;
    private final SecretKey secretKey;

    public JwtManagerImpl(String secretKey, long expirationMinutes, Clock clock) {
        this.expirationMinutes = expirationMinutes;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.clock = clock;
    }

    public JwtManagerImpl(String secretKey, long expirationMinutes) {
        this(secretKey, expirationMinutes, Clock.systemDefaultZone());
    }

    @Override
    public @NotNull String generateToken(@NotNull JwtUser user) {
        LocalDateTime now = LocalDateTime.now(clock);
        Instant accessExpirationInstant = now.plusMinutes(expirationMinutes)
                .atZone(ZoneId.systemDefault()).toInstant();
        Date expiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(expiration)
                .signWith(secretKey)
                .claim(USERNAME_CLAIM, user.getUsername())
                .compact();
    }

    @Override
    public boolean validateToken(@NotNull String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ignored) {
            return false;
        }
    }

    @Override
    public @NotNull JwtUser parseUser(@NotNull String token) {
        Claims claims = getClaims(token);
        return new JwtUser(
                UUID.fromString(claims.getSubject()),
                claims.get(USERNAME_CLAIM, String.class)
        );
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
