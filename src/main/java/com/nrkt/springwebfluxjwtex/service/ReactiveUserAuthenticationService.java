package com.nrkt.springwebfluxjwtex.service;

import com.nrkt.springwebfluxjwtex.error.exception.UserInformationWrongException;
import com.nrkt.springwebfluxjwtex.error.exception.UserNotActiveException;
import com.nrkt.springwebfluxjwtex.error.exception.UserNotFoundException;
import com.nrkt.springwebfluxjwtex.model.User;
import com.nrkt.springwebfluxjwtex.payload.request.LoginDTO;
import com.nrkt.springwebfluxjwtex.payload.response.LoginResponseDTO;
import com.nrkt.springwebfluxjwtex.repository.ReactiveUserRepository;
import com.nrkt.springwebfluxjwtex.security.util.jwt.TokenProvider;
import com.nrkt.springwebfluxjwtex.util.LogErrorUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Service("userAuthenticationService")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
@Transactional
public class ReactiveUserAuthenticationService implements ReactiveUserDetailsService {

    ReactiveUserRepository reactiveUserRepository;
    BCryptPasswordEncoder passwordEncoder;
    TokenProvider tokenProvider;

    @Override
    public Mono<UserDetails> findByUsername(@Nonnull final String username) {
        return reactiveUserRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username not found!")))
                .map(userDetails -> userDetails);
    }

    public Mono<User> findByUserEmail(@Nonnull final String email) {
        return reactiveUserRepository.findOneByEmailIgnoreCase(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User for this e-mail does not exist!")));
    }

    public Mono<UsernamePasswordAuthenticationToken> usernamePasswordAuthentication(@Nonnull final String mail) {
        return findByUserEmail(mail)
                .map(user -> new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    public Mono<LoginResponseDTO> login(@Nonnull final LoginDTO loginPayload) {
        return findByUserEmail(loginPayload.getEmail())
                .map(user -> {
                    if (Boolean.TRUE.equals(user.isEnabled())) {
                        if (passwordEncoder.matches(loginPayload.getPassword(), user.getPassword())) {
                            return user;
                        }
                        LogErrorUtils.errorLogger("attempted wrong password for this email: " + loginPayload.getEmail());
                        throw new UserInformationWrongException("User information is not valid!");
                    }
                    throw new UserNotActiveException("User is not an active user!");
                }).flatMap(auth ->
                        Mono.fromCallable(() ->
                                tokenProvider.generateJwtToken(auth)).map(LoginResponseDTO::new));
    }
}
