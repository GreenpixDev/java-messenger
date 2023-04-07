package ru.greenpix.messenger.user.model;

import lombok.Data;

import java.util.UUID;

@Data
public class JwtUser {

    private final UUID id;

    private final String username;


}
