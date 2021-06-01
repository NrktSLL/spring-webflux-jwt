package com.nrkt.springwebfluxjwtex.security;

import com.nrkt.springwebfluxjwtex.security.util.SecurityUtils;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
public class AuditorAware implements ReactiveAuditorAware<String> {

    public static final String SYSTEM = "system";

    @Override
    @Nonnull
    public Mono<String> getCurrentAuditor() {
        return SecurityUtils.getCurrentUserLogin()
                .map(currentUser -> {
                    if (currentUser.isEmpty()) currentUser = SYSTEM;
                    return currentUser;
                });
    }
}
