package com.analizegithubusers.config;

import com.analizegithubusers.security.SecurityContextRepository;
import com.analizegithubusers.security.AuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http.cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))).and()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/api/v1/auth/**").permitAll()
                .pathMatchers("/docs/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("ADMIN")
                .pathMatchers("/api/v1/github-users/**").authenticated()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}