package ru.greenpix.messenger.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordSecurityConfiguration {

    /**
     * Bean encoder'а паролей через алгоритм хеширования BCrypt
     * @return encoder паролей BCrypt
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
