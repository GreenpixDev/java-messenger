package ru.greenpix.messenger.integration.role;

import org.springframework.security.core.GrantedAuthority;

public class SystemRole implements GrantedAuthority {

    public static final String NAME = "SYSTEM";

    @Override
    public String getAuthority() {
        return "ROLE_" + NAME;
    }
}