package ru.greenpix.messenger.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;
import ru.greenpix.messenger.auth.manager.JwtManager;
import ru.greenpix.messenger.auth.model.JwtUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Некоторый код позаимствован с {@link org.springframework.security.web.authentication.www.BasicAuthenticationConverter}
 */
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final String AUTHORIZATION_SCHEME_BEARER = "Bearer";

    private final JwtManager jwtManager;
    private final GrantedAuthority grantedAuthority;

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
        if (!jwtManager.validateToken(token)) {
            throw new BadCredentialsException("Invalid bearer authentication jwt token");
        }

        JwtUser user = jwtManager.parseUser(token);

        return UsernamePasswordAuthenticationToken
                .authenticated(user, null, List.of(grantedAuthority));
    }
}
