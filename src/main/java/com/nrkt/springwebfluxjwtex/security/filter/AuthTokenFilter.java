package com.nrkt.springwebfluxjwtex.security.filter;

import com.nrkt.springwebfluxjwtex.security.util.jwt.TokenProvider;
import com.nrkt.springwebfluxjwtex.service.ReactiveUserAuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class AuthTokenFilter implements WebFilter {

    public static final String HEADER_PREFIX = "Bearer ";

    TokenProvider tokenProvider;
    ReactiveUserAuthenticationService authService;

    @Override
    @Nonnull
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        String token = resolveToken(exchange.getRequest());
        if (StringUtils.hasText(token) && this.tokenProvider.validateJwtToken(token).equals(true)) {
            var userMail = tokenProvider.getSubject(token); //subject --> mail
            return authService.usernamePasswordAuthentication(userMail)
                    .flatMap(auth -> chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))).log();
        }

        return chain.filter(exchange);
    }

    private String resolveToken(@Nonnull ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX) ? bearerToken.substring(7) : "";
    }
}
