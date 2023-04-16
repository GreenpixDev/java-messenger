package ru.greenpix.messenger.user.model;

import lombok.Data;

@Data
public class UserSort {

    private final UserAttribute attribute;
    private final boolean descending;

    public static UserSort of(String str) {
        String[] data = str.split(":", 2);
        boolean desc = data.length <= 1 || !data[1].equalsIgnoreCase("asc");
        return new UserSort(UserAttribute.getByName(data[0]), desc);
    }

}
