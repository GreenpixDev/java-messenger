package ru.greenpix.messenger.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class SignUpDto {

    @NotBlank
    private final String username;

    @NotBlank
    private final String fullName;

    private final LocalDate birthDate;

    @Size(min = 6, max = 50)
    private final String password;

}
