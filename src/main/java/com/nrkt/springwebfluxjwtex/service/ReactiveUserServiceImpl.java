package com.nrkt.springwebfluxjwtex.service;

import com.nrkt.springwebfluxjwtex.error.exception.EmailAlreadyUsedException;
import com.nrkt.springwebfluxjwtex.mapper.UserMapper;
import com.nrkt.springwebfluxjwtex.model.User;
import com.nrkt.springwebfluxjwtex.model.UserRole;
import com.nrkt.springwebfluxjwtex.payload.request.UserRequestDTO;
import com.nrkt.springwebfluxjwtex.payload.response.UserResponseDTO;
import com.nrkt.springwebfluxjwtex.repository.ReactiveUserRepository;
import com.nrkt.springwebfluxjwtex.security.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class ReactiveUserServiceImpl implements ReactiveUserService {

    ReactiveUserRepository reactiveUserRepository;
    UserMapper userMapper;

    @Override
    public Mono<UserResponseDTO> addUser(UserRequestDTO userRequestDTO) {
        return reactiveUserRepository.findOneByEmailIgnoreCase(userRequestDTO.getEmail())
                .hasElement()
                .filter(Boolean.TRUE::equals)
                .flatMap(error -> Mono.error(new EmailAlreadyUsedException("Email is already in use!")))
                .then(Mono.just(userRequestDTO))
                .map(userMapper::userDtoToUser)
                .map(user -> {
                    user.setUserRole(List.of(UserRole.ROLE_USER));
                    return user;
                })
                .flatMap(this::addOrUpdateUser)
                .map(userMapper::userToUserResponse);
    }

    @Override
    public Mono<UserResponseDTO> editUser(String id, UserRequestDTO userRequestDTO) {
        return validateAuditor(id)
                .flatMap(email -> reactiveUserRepository.findOneByEmailIgnoreCase(userRequestDTO.getEmail()))
                .map(user -> !user.getId().equals(id))
                .flatMap(error -> Mono.error(new EmailAlreadyUsedException("Email is already in use!")))
                .then(Mono.just(userRequestDTO))
                .map(userMapper::userDtoToUser)
                .flatMap(user -> findUser(id).map(newUser -> {
                    newUser.setId(id);
                    newUser.setEmail(user.getEmail());
                    newUser.setName(user.getName());
                    return newUser;
                }))
                .flatMap(this::addOrUpdateUser)
                .map(userMapper::userToUserResponse);
    }

    @Override
    public Mono<Void> removeUser(String id) {
        return findUser(id).flatMap(reactiveUserRepository::delete).log();
    }

    @Override
    public Mono<UserResponseDTO> getUser(String id) {
        return findUser(id).map(userMapper::userToUserResponse);
    }

    @Override
    public Mono<Void> assignRole(String id, UserRole userRole) {
        return findUser(id).map(user -> {
            user.setUserRole(Collections.singletonList(userRole));
            return user;
        }).flatMap(reactiveUserRepository::save).then().log();
    }

    @Override
    public Mono<Void> editStatus(String id, Boolean active) {
        return validateAuditor(id)
                .then(findUser(id).map(user -> {
                    user.setActive(active);
                    return user;
                }).flatMap(reactiveUserRepository::save).then().log());
    }

    private Mono<User> findUser(String userId) {
        return reactiveUserRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("No such user exists!")));
    }

    private Mono<User> addOrUpdateUser(User user) {
        return reactiveUserRepository.save(user).log();
    }

    private Mono<User> validateAuditor(String id) {
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(reactiveUserRepository::findByUsername)
                .map(user -> {
                    if (user.getId().equals(id)) {
                        return user;
                    } else if (user.getUserRole().stream().noneMatch(UserRole.ROLE_ADMIN::equals)) {
                        throw new AccessDeniedException("Unauthorized access !");
                    }
                    return user;
                });
    }
}
