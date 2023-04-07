package ru.greenpix.messenger.user.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.user.model.JwtUser;
import ru.greenpix.messenger.user.service.JwtService;
import ru.greenpix.messenger.user.settings.JwtSettings;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtSettings jwtSettings;

    private final SecretKey secretKey;

    public JwtServiceImpl(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSettings.getSecret()));
    }

    @Override
    public String generateToken(JwtUser user) {
        LocalDateTime now = LocalDateTime.now();
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
    public boolean validateToken(String token) {
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
    public JwtUser getUser(String token) {
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
