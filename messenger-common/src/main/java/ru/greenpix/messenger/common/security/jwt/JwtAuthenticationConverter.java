package ru.greenpix.messenger.common.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.common.security.role.UserRole;
import ru.greenpix.messenger.common.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Некоторый код позаимствован с {@link org.springframework.security.web.authentication.www.BasicAuthenticationConverter}
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final String AUTHORIZATION_SCHEME_BEARER = "Bearer";

    private final JwtService jwtService;
    private final UserRole userRole;

    @Override
    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }

        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHORIZATION_SCHEME_BEARER)) {
            return null;
        }
        if (header.equalsIgnoreCase(AUTHORIZATION_SCHEME_BEARER)) {
            throw new BadCredentialsException("Empty bearer authentication token");
        }

        String token = header.substring(AUTHORIZATION_SCHEME_BEARER.length() + 1);
        if (!jwtService.validateToken(token)) {
            throw new BadCredentialsException("Invalid bearer authentication jwt token");
        }

        JwtUser user = jwtService.parseUser(token);

        return UsernamePasswordAuthenticationToken
                .authenticated(user, null, List.of(userRole));
    }
}
