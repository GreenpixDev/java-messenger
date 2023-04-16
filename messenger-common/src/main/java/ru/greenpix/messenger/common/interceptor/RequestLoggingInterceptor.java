package ru.greenpix.messenger.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public @NotNull ClientHttpResponse intercept(
            @NotNull HttpRequest request,
            byte @NotNull [] body,
            @NotNull ClientHttpRequestExecution execution
    ) throws IOException {
        log.info("Integration request [{} {}]", request.getMethodValue(), request.getURI());
        return execution.execute(request, body);
    }
}
