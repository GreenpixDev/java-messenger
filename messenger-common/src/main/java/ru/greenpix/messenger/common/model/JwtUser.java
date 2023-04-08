package ru.greenpix.messenger.common.model;

import lombok.Data;

import java.util.UUID;

@Data
public class JwtUser {

    private final UUID id;

    private final String username;

}
