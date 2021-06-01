package com.nrkt.springwebfluxjwtex.controller;

import com.nrkt.springwebfluxjwtex.payload.request.LoginDTO;
import com.nrkt.springwebfluxjwtex.service.ReactiveUserAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("v1/login")
@Tag(name = "auth", description = "Authentication  Service")
public class AuthController {

    ReactiveUserAuthenticationService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Object>> login(@RequestBody @Valid LoginDTO loginPayload) {
        return authService.login(loginPayload).map(
                jwt -> {
                    var httpHeaders = new HttpHeaders();
                    httpHeaders.add("Authorization", "Bearer " + jwt.getToken());
                    return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
                }
        );
    }
}
