package ru.greenpix.messenger.user.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Data
public class UserSortListDto implements Serializable {

    private final Sort.Direction sortId;
    private final Sort.Direction sortRegistrationDate;
    private final Sort.Direction sortUsername;
    private final Sort.Direction sortEmail;
    private final Sort.Direction sortFullName;
    private final Sort.Direction sortBirthDate;
    private final Sort.Direction sortPhone;
    private final Sort.Direction sortCity;
    private final Sort.Direction sortAvatar;

}
