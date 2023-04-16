package ru.greenpix.messenger.friends.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class BlockedUserSearchDto implements Serializable {

    private final LocalDate additionDate;
    private final String fullName;

}
