package ru.greenpix.messenger.jwt.role;

import org.springframework.security.core.GrantedAuthority;

public class UserRole implements GrantedAuthority {

    public static final String NAME = "USER";

    @Override
    public String getAuthority() {
        return "ROLE_" + NAME;
    }
}
