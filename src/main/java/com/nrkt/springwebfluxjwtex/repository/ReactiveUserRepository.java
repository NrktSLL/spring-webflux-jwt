package com.nrkt.springwebfluxjwtex.repository;

import com.nrkt.springwebfluxjwtex.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findByUsername(String username);
}
