package com.nrkt.springwebfluxjwtex.security.config;

import com.nrkt.springwebfluxjwtex.security.filter.AuthTokenFilter;
import com.nrkt.springwebfluxjwtex.security.util.jwt.TokenProvider;
import com.nrkt.springwebfluxjwtex.service.ReactiveUserAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Import(SecurityProblemSupport.class)
public class WebSecurityConfig {

    BCryptPasswordEncoder passwordEncoder;
    TokenProvider tokenProvider;
    ReactiveUserAuthenticationService reactiveUserAuthenticationService;
    SecurityProblemSupport problemSupport;

    private static final String[] WhiteListSwagger = {
            "/documentation/**",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .headers().frameOptions().disable().and()
                .exceptionHandling().accessDeniedHandler(problemSupport).authenticationEntryPoint(problemSupport).and()
                .addFilterAt(new AuthTokenFilter(tokenProvider, reactiveUserAuthenticationService), SecurityWebFiltersOrder.AUTHENTICATION)
                .authenticationManager(reactiveAuthenticationManager())
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/v1/login/**").permitAll()
                .pathMatchers(HttpMethod.PATCH, "/v1/users/status/**").hasAnyRole("USER")
                .pathMatchers(HttpMethod.POST, "/v1/users/**").permitAll()
                .pathMatchers(HttpMethod.PUT, "/v1/users/**").hasAnyRole("ADMIN", "USER")
                .pathMatchers("/v1/users/**").hasAnyRole("ADMIN")
                .pathMatchers(WhiteListSwagger).permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserAuthenticationService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}
