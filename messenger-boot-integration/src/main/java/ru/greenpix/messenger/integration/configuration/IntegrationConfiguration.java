package ru.greenpix.messenger.integration.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import ru.greenpix.messenger.integration.interceptor.RequestLoggingInterceptor;
import ru.greenpix.messenger.integration.interceptor.RestTemplateApiKeyInterceptor;
import ru.greenpix.messenger.integration.settings.SecurityApiSettings;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({SecurityApiSettings.class})
public class IntegrationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(
            RestTemplateApiKeyInterceptor apiKeyInterceptor,
            RequestLoggingInterceptor loggingInterceptor
    ) {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(apiKeyInterceptor);
        interceptors.add(loggingInterceptor);
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateApiKeyInterceptor apiKeyInterceptor(SecurityApiSettings settings) {
        return new RestTemplateApiKeyInterceptor(settings.getKey());
    }

    @Bean
    public RequestLoggingInterceptor requestLoggingInterceptor() {
        return new RequestLoggingInterceptor();
    }

}
