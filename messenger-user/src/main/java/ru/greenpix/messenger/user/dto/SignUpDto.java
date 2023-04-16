package ru.greenpix.messenger.user.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class SignUpDto {

    @NotBlank
    private final String username;

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private final String fullName;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthDate;

    private final String phone;

    private final String city;

    @Size(min = 6, max = 50)
    private final String password;

}
