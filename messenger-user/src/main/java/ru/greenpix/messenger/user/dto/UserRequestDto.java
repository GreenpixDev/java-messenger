package ru.greenpix.messenger.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRequestDto {

    @NotBlank
    private final String fullName;

    private final LocalDate birthDate;

    private final String phone;

    private final String city;

    private final UUID avatarId;

}