package ru.greenpix.messenger.common.security.key;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.common.provider.ApiKeyProvider;
import ru.greenpix.messenger.common.security.role.SystemRole;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Некоторый код позаимствован с {@link org.springframework.security.web.authentication.www.BasicAuthenticationConverter}
 */
@RequiredArgsConstructor
@Component
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

    private static final String API_KEY_PARAM_NAME = "api-key";

    private final ApiKeyProvider apiKeyProvider;
    private final SystemRole systemRole;

    @Override
    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        String apiKey = request.getParameter(API_KEY_PARAM_NAME);

        if (apiKey == null) {
            return null;
        }

        if (!apiKey.equals(apiKeyProvider.getApiKey())) {
            throw new BadCredentialsException("Invalid api key");
        }

        return UsernamePasswordAuthenticationToken
                .authenticated(apiKey, null, List.of(systemRole));
    }
}
