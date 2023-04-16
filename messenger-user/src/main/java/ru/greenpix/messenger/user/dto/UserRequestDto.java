package ru.greenpix.messenger.user.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserRequestDto {

    @NotBlank
    private final String fullName;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthDate;

    private final String phone;

    private final String city;

    private final UUID avatarId;

}