package ru.greenpix.messenger.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.greenpix.messenger.common.security.jwt.JwtAuthenticationFilter;
import ru.greenpix.messenger.common.security.key.ApiKeyAuthenticationFilter;
import ru.greenpix.messenger.common.security.role.SystemRole;
import ru.greenpix.messenger.common.security.role.UserRole;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http,
                                                      JwtAuthenticationFilter jwtFilter,
                                                      ApiKeyAuthenticationFilter apiKeyFilter)
            throws Exception {

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

                .authorizeHttpRequests(request -> request
                        .antMatchers(
                                "/signin",
                                "/signup",
                                "/swagger",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .antMatchers("/api/**").hasRole(SystemRole.NAME)
                        .anyRequest().hasRole(UserRole.NAME)

                        .and()
                        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .addFilterAfter(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                )

                .build();
    }

    @Bean
    public SystemRole systemRole() {
        return new SystemRole();
    }

    @Bean
    public UserRole userRole() {
        return new UserRole();
    }
}
