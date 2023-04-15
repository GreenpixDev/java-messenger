package ru.greenpix.messenger.common.interceptor;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.greenpix.messenger.common.provider.ApiKeyProvider;

import java.io.IOException;
import java.net.URI;

// https://stackoverflow.com/questions/57696932/adding-request-param-to-every-request-using-spring-resttemplate
@Component
@RequiredArgsConstructor
public class RestTemplateApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private final ApiKeyProvider apiKeyProvider;

    @Override
    public @NotNull ClientHttpResponse intercept(
            @NotNull HttpRequest request,
            byte @NotNull [] body,
            @NotNull ClientHttpRequestExecution execution
    ) throws IOException {
        URI uri = UriComponentsBuilder.fromHttpRequest(request)
                .queryParam(ApiKeyConst.PARAM_NAME, apiKeyProvider.getApiKey())
                .build().toUri();

        HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
            @Override
            public @NotNull URI getURI() {
                return uri;
            }
        };

        return execution.execute(modifiedRequest, body);
    }
}
