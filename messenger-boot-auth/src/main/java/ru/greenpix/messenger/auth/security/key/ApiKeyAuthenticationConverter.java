package ru.greenpix.messenger.auth.security.key;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.auth.ApiKeyConst;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Некоторый код позаимствован с {@link org.springframework.security.web.authentication.www.BasicAuthenticationConverter}
 */
@RequiredArgsConstructor
@Component
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

    private final String apiKey;
    private final GrantedAuthority grantedAuthority;

    @Override
    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        String apiKey = request.getParameter(ApiKeyConst.PARAM_NAME);

        if (apiKey == null) {
            return null;
        }

        if (!apiKey.equals(this.apiKey)) {
            throw new BadCredentialsException("Invalid api key");
        }

        return UsernamePasswordAuthenticationToken
                .authenticated(apiKey, null, List.of(grantedAuthority));
    }
}
