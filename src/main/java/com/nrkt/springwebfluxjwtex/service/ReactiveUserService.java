package com.nrkt.springwebfluxjwtex.service;

import com.nrkt.springwebfluxjwtex.model.UserRole;
import com.nrkt.springwebfluxjwtex.payload.request.UserRequestDTO;
import com.nrkt.springwebfluxjwtex.payload.response.UserResponseDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

public interface ReactiveUserService {

    /**
     * Create new user.
     *
     * @param userRequestDTO user information.
     * @return a completed {@link Mono}.
     * @throws com.nrkt.springwebfluxjwtex.error.exception.EmailAlreadyUsedException on error
     */
    Mono<UserResponseDTO> addUser(UserRequestDTO userRequestDTO);

    /**
     * Update basic information (name, password, email) user.
     *
     * @param id             user id.
     * @param userRequestDTO user information.
     * @return a completed {@link Mono}.
     * @throws com.nrkt.springwebfluxjwtex.error.exception.EmailAlreadyUsedException on error if email is available.
     * @throws UsernameNotFoundException                                             If there is no such user, it will generate such an error.
     */
    Mono<UserResponseDTO> editUser(String id, UserRequestDTO userRequestDTO);

    /**
     * Delete user
     *
     * @param id user id.
     * @return a completed {@link Mono}.
     * @throws UsernameNotFoundException If there is no such user, it will generate such an error.
     */
    Mono<Void> removeUser(String id);

    /**
     * Fetch user by id
     *
     * @param id user id.
     * @return a completed {@link Mono}.
     * @throws UsernameNotFoundException If there is no such user, it will generate such an error.
     */
    Mono<UserResponseDTO> getUser(String id);

    /**
     * assign role for user
     *
     * @param id       user id.
     * @param userRole assignable user roles. {@link UserRole}
     * @return a completed {@link Mono}.
     * @throws UsernameNotFoundException If there is no such user, it will generate such an error.
     */
    Mono<Void> assignRole(String id, UserRole userRole);

    /**
     * Changing the active status of users
     *
     * @param id     user id.
     * @param active user status. (TRUE,FALSE)
     * @return a completed {@link Mono}.
     * @throws UsernameNotFoundException If there is no such user, it will generate such an error.
     */
    Mono<Void> editStatus(String id, Boolean active);
}
