package ru.greenpix.messenger.integration.interceptor;

import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Log
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public @NotNull ClientHttpResponse intercept(
            @NotNull HttpRequest request,
            byte @NotNull [] body,
            @NotNull ClientHttpRequestExecution execution
    ) throws IOException {
        log.info("Integration request [" + request.getMethodValue() + " " + request.getURI() + "]");
        return execution.execute(request, body);
    }
}
