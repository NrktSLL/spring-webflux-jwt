package com.nrkt.springwebfluxjwtex.controller;

import com.nrkt.springwebfluxjwtex.model.UserRole;
import com.nrkt.springwebfluxjwtex.payload.request.UserRequestDTO;
import com.nrkt.springwebfluxjwtex.payload.response.UserResponseDTO;
import com.nrkt.springwebfluxjwtex.service.ReactiveUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("v1/users")
@Tag(name = "user", description = "User Service")
public class UserController {

    ReactiveUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create User")
    public Mono<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit User")
    @SecurityRequirement(name = "Bearer Auth")
    public Mono<UserResponseDTO> updateUser(@PathVariable String id, UserRequestDTO user) {
        return userService.editUser(id, user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get User")
    @SecurityRequirement(name = "Bearer Auth")
    public Mono<UserResponseDTO> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PatchMapping("role/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Assign User Role")
    @SecurityRequirement(name = "Bearer Auth")
    public Mono<Void> assignToRole(@PathVariable String id, @RequestHeader UserRole userRole) {
        return userService.assignRole(id, userRole);
    }

    @PatchMapping("status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Change Active Status Users")
    @SecurityRequirement(name = "Bearer Auth")
    public Mono<Void> editUserActiveStatus(@PathVariable String id, @RequestHeader Boolean active) {
        return userService.editStatus(id, active);
    }
}
