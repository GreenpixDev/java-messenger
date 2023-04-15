package ru.greenpix.messenger.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    /**
     * Необходим для возможности покрытия unit тестами методов, в которых используется
     * {@link java.time.LocalDate#now(Clock)}, {@link java.time.LocalTime#now(Clock)}
     * или {@link java.time.LocalDateTime#now(Clock)}
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
