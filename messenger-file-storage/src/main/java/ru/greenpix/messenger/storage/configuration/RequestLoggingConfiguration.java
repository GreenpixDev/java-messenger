package ru.greenpix.messenger.storage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

@Configuration
public class RequestLoggingConfiguration {

    /**
     * Bean логгера входящих HTTP запросов
     */
    @Bean
    public AbstractRequestLoggingFilter requestLoggingFilter() {
        ServletContextRequestLoggingFilter filter = new ServletContextRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        return filter;
    }

}
