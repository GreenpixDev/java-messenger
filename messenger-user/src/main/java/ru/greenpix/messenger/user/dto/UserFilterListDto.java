package ru.greenpix.messenger.user.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserFilterListDto implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate filterRegistrationDate;

    private final String filterUsername;

    private final String filterEmail;

    private final String filterFullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate filterBirthDate;

    private final String filterPhone;

    private final String filterCity;

}
