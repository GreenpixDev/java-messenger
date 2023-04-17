package ru.greenpix.messenger.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationConverter;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationFilter;
import ru.greenpix.messenger.jwt.security.JwtAuthenticationConverter;
import ru.greenpix.messenger.jwt.security.JwtAuthenticationFilter;

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
            JwtAuthenticationConverter jwtConverter,
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
                        "/signin",
                        "/signup",
                        "/swagger",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()

                .and()
                .addFilterAfter(new JwtAuthenticationFilter(
                        jwtConverter,
                        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**"))
                ), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new ApiKeyAuthenticationFilter(
                        apiKeyConverter,
                        new AntPathRequestMatcher("/api/**")
                ), UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
