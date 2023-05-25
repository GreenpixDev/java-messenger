package ru.greenpix.messenger.common.dto.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListIntegrationDto {

    private List<UserIntegrationDto> users;

}
