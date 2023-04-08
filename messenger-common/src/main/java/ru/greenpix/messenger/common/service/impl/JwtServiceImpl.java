package ru.greenpix.messenger.common.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.common.provider.JwtSettingsProvider;
import ru.greenpix.messenger.common.service.JwtService;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtSettingsProvider jwtSettings;

    private final Clock clock;

    private final SecretKey secretKey;

    public JwtServiceImpl(JwtSettingsProvider jwtSettings, Clock clock) {
        this.jwtSettings = jwtSettings;
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSettings.getSecret()));
        this.clock = clock;
    }

    @Override
    public String generateToken(@NotNull JwtUser user) {
        LocalDateTime now = LocalDateTime.now(clock);
        Instant accessExpirationInstant = now.plusMinutes(jwtSettings.getExpirationMinutes())
                .atZone(ZoneId.systemDefault()).toInstant();
        Date expiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(expiration)
                .signWith(secretKey)
                .claim("username", user.getUsername())
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
    public JwtUser parseUser(@NotNull String token) {
        Claims claims = getClaims(token);
        return new JwtUser(
                UUID.fromString(claims.getSubject()),
                claims.get("username", String.class)
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
