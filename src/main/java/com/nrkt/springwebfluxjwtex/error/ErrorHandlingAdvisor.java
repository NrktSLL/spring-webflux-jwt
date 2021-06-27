package com.nrkt.springwebfluxjwtex.error;

import com.nrkt.springwebfluxjwtex.error.exception.EmailAlreadyUsedException;
import com.nrkt.springwebfluxjwtex.error.exception.UserInformationWrongException;
import com.nrkt.springwebfluxjwtex.error.exception.UserNotActiveException;
import com.nrkt.springwebfluxjwtex.error.exception.UserNotFoundException;
import com.nrkt.springwebfluxjwtex.util.LogErrorUtils;
import com.nrkt.springwebfluxjwtex.util.ProblemBuilder;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.security.SecurityAdviceTrait;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingAdvisor implements ProblemHandling, SecurityAdviceTrait {

    @Override
    @ExceptionHandler(AccessDeniedException.class)
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    @ResponseStatus(FORBIDDEN)
    public Mono<ResponseEntity<Problem>> handleAccessDenied(AccessDeniedException accessDeniedException, ServerWebExchange request) {
        LogErrorUtils.errorLogger(accessDeniedException, FORBIDDEN, request);
        return SecurityAdviceTrait.super.handleAccessDenied(accessDeniedException, request);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    @ResponseStatus(CONFLICT)
    public Mono<ResponseEntity<Problem>> emailAlreadyUsedExceptionHandler(EmailAlreadyUsedException emailAlreadyUsedException, ServerWebExchange request) {
        LogErrorUtils.errorLogger(emailAlreadyUsedException, request);
        return create(emailAlreadyUsedException, request);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            UserNotFoundException.class})
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    @ResponseStatus(NOT_FOUND)
    public Mono<ResponseEntity<Problem>> notFoundExceptionHandler(Exception notFoundExceptionException, ServerWebExchange request) {
        var problem =
                ProblemBuilder.createProblem("User", notFoundExceptionException.getMessage(), notFoundExceptionException, Status.NOT_FOUND);

        LogErrorUtils.errorLogger(problem, request);
        return create(notFoundExceptionException, request);
    }

    @ExceptionHandler({
            UserInformationWrongException.class,
            UserNotActiveException.class})
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    @ResponseStatus(UNAUTHORIZED)
    public Mono<ResponseEntity<Problem>> userAuthenticationHandler(Exception userInformationException, ServerWebExchange request) {
        var problem =
                ProblemBuilder.createProblem("User", "User information is not valid!", userInformationException, Status.UNAUTHORIZED);

        LogErrorUtils.errorLogger(userInformationException.getMessage(), UNAUTHORIZED, request);
        return create(userInformationException, problem, request);
    }
}
