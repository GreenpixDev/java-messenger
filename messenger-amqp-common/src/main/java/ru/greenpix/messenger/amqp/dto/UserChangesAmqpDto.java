package ru.greenpix.messenger.amqp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserChangesAmqpDto {

    private UUID id;

    private String fullName;

    private UUID avatarId;

}
