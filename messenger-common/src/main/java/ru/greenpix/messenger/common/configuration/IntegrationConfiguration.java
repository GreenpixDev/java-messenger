package ru.greenpix.messenger.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import ru.greenpix.messenger.common.interceptor.RestTemplateApiKeyInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class IntegrationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateApiKeyInterceptor apiKeyInterceptor) {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(apiKeyInterceptor);
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
