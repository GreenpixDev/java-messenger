package ru.greenpix.messenger.common.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.greenpix.messenger.common.configuration.ClockConfiguration;
import ru.greenpix.messenger.common.configuration.LoggingConfiguration;
import ru.greenpix.messenger.common.configuration.RequestLoggingConfiguration;
import ru.greenpix.messenger.common.configuration.WebSecurityConfiguration;
import ru.greenpix.messenger.common.controller.advice.ValidationAdvice;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.common.mapper.impl.PageMapperImpl;

@Configuration
@Import({
        ClockConfiguration.class,
        LoggingConfiguration.class,
        RequestLoggingConfiguration.class,
        WebSecurityConfiguration.class
})
public class CommonAutoConfiguration {

    @Bean
    public ValidationAdvice validationAdvice() {
        return new ValidationAdvice();
    }

    @Bean
    public PageMapper pageMapper() {
        return new PageMapperImpl();
    }

}
