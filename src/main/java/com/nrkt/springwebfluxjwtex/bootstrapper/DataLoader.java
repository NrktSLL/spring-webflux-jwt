package com.nrkt.springwebfluxjwtex.bootstrapper;

import com.nrkt.springwebfluxjwtex.model.User;
import com.nrkt.springwebfluxjwtex.model.UserRole;
import com.nrkt.springwebfluxjwtex.repository.ReactiveUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ReactiveUserRepository reactiveMongoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String USERNAME = "super_user";
    private static final String PASSWORD = "root";
    private static final String EMAIL = "aa@aa.com";

    @Override
    public void run(String... args) {
        var adminUser = User.builder()
                .email(EMAIL)
                .active(true)
                .name("admin")
                .username(USERNAME)
                .password(passwordEncoder.encode(PASSWORD))
                .userRole(List.of(UserRole.ROLE_ADMIN, UserRole.ROLE_USER))
                .build();

        reactiveMongoRepository
                .findByUsername(USERNAME)
                .switchIfEmpty(reactiveMongoRepository.save(adminUser))
                .subscribe();

    }
}
