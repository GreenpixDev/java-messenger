package ru.greenpix.messenger.jwt.model;

import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Data
public class JwtUser implements Principal {

    private final UUID id;

    private final String username;

    @Override
    public String getName() {
        return username;
    }
}
