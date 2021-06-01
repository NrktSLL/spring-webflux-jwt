package com.nrkt.springwebfluxjwtex.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;

import java.time.LocalDateTime;

@Slf4j
@UtilityClass
public class LogErrorUtils {

    public void errorLogger(Problem problem, ServerWebExchange request) {
        log.error(problem.getDetail() + " " + problem.getStatus() + " " + request.getRequest().getURI() + " " + LocalDateTime.now());
    }

    public void errorLogger(Exception exception, HttpStatus httpStatus, ServerWebExchange request) {
        log.error(exception.getMessage() + " " + httpStatus + " " + request.getRequest().getURI() + " " + LocalDateTime.now());
    }

    public void errorLogger(String message, HttpStatus httpStatus, ServerWebExchange request) {
        log.error(message + " " + httpStatus + " " + request.getRequest().getURI() + " " + LocalDateTime.now());
    }

    public void errorLogger(Exception exception) {
        log.error(exception.getMessage());
    }

    public void errorLogger(String message) {
        log.error(message);
    }
}
