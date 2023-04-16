package ru.greenpix.messenger.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import ru.greenpix.messenger.auth.security.JwtAuthenticationConverter;
import ru.greenpix.messenger.auth.security.JwtAuthenticationFilter;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationConverter;
import ru.greenpix.messenger.integration.security.ApiKeyAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    @Order(10000)
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
