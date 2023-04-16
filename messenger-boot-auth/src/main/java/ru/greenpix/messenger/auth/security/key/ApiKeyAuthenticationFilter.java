package ru.greenpix.messenger.auth.security.key;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Некоторый код позаимствован с {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter}
 */
@RequiredArgsConstructor
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyAuthenticationConverter authenticationConverter;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain)
            throws ServletException, IOException {

        try {
            Authentication authRequest = authenticationConverter.convert(request);

            if (authRequest == null) {
                logger.trace("Did not process authentication request since failed to find "
                        + "api key in request params");
                chain.doFilter(request, response);
                return;
            }

            String apiKey = authRequest.getName();
            logger.trace(LogMessage.format("Found api key '%s' in request params", apiKey));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authRequest);
            SecurityContextHolder.setContext(context);
        }
        catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            logger.debug("Failed to process authentication request", ex);
        }

        chain.doFilter(request, response);
    }
}
