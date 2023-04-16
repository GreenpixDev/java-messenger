package ru.greenpix.messenger.user.model;

import lombok.Data;

@Data
public class UserFilter {

    private final UserAttribute attribute;
    private final String value;

    public static UserFilter of(String str) {
        String[] data = str.split(":", 2);
        String value = data.length > 1 ? data[1] : "";
        return new UserFilter(UserAttribute.getByName(data[0]), value);
    }

}
