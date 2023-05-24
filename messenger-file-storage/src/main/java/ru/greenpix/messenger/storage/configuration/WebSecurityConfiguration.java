package ru.greenpix.messenger.storage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationConverter;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * Bean настройки spring web security.
     * В настройке учитываются авторизации по JWT и API-KEY.
     * <p>
     * Аннотация {@link Order} нужна для корректной работы.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityWebFilterChain(
            HttpSecurity http,
            ApiKeyAuthenticationConverter apiKeyConverter
    ) throws Exception {
        return http
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()

                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers(
                        "/swagger",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .antMatchers(HttpMethod.GET, "/storage/file/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterAfter(new ApiKeyAuthenticationFilter(
                        apiKeyConverter,
                        new AntPathRequestMatcher("/api/**")
                ), UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
